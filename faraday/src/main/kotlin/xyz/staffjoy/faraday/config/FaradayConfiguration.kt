package xyz.staffjoy.faraday.config

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.Ordered
import xyz.staffjoy.faraday.config.StaffjoyWebConfig
import xyz.staffjoy.faraday.env.EnvConfig
import xyz.staffjoy.faraday.core.balancer.LoadBalancer
import xyz.staffjoy.faraday.core.balancer.RandomLoadBalancer
import xyz.staffjoy.faraday.core.filter.FaviconFilter
import xyz.staffjoy.faraday.core.filter.HealthCheckFilter
import xyz.staffjoy.faraday.core.filter.NakedDomainFilter
import xyz.staffjoy.faraday.core.filter.SecurityFilter
import xyz.staffjoy.faraday.core.http.HttpClientProvider
import xyz.staffjoy.faraday.core.http.RequestDataExtractor
import xyz.staffjoy.faraday.core.http.RequestForwarder
import xyz.staffjoy.faraday.core.http.ReverseProxyFilter
import xyz.staffjoy.faraday.core.interceptor.AuthRequestInterceptor
import xyz.staffjoy.faraday.core.interceptor.CacheResponseInterceptor
import xyz.staffjoy.faraday.core.interceptor.PostForwardResponseInterceptor
import xyz.staffjoy.faraday.core.interceptor.PreForwardRequestInterceptor
import xyz.staffjoy.faraday.core.mappings.ConfigurationMappingsProvider
import xyz.staffjoy.faraday.core.mappings.MappingsProvider
import xyz.staffjoy.faraday.core.mappings.MappingsValidator
import xyz.staffjoy.faraday.core.mappings.ProgrammaticMappingsProvider
import xyz.staffjoy.faraday.core.trace.LoggingTraceInterceptor
import xyz.staffjoy.faraday.core.trace.ProxyingTraceInterceptor
import xyz.staffjoy.faraday.core.trace.TraceInterceptor
import xyz.staffjoy.faraday.view.AssetLoader
import java.util.*

@Configuration
@EnableConfigurationProperties(FaradayProperties::class, StaffjoyPropreties::class)
@Import(value = [StaffjoyWebConfig::class])
class FaradayConfiguration(protected val faradayProperties: FaradayProperties,
                           protected val serverProperties: ServerProperties,
                           protected val staffjoyPropreties: StaffjoyPropreties,
                           protected val assetLoader: AssetLoader) {
    @Bean
    fun faradayReverseProxyFilterRegistrationBean(
            proxyFilter: ReverseProxyFilter): FilterRegistrationBean<ReverseProxyFilter> {
        val registrationBean = FilterRegistrationBean(proxyFilter)
        registrationBean.order = faradayProperties.filterOrder // by default to Ordered.HIGHEST_PRECEDENCE + 100
        return registrationBean
    }

    @Bean
    fun nakedDomainFilterRegistrationBean(envConfig: EnvConfig?): FilterRegistrationBean<NakedDomainFilter> {
        val registrationBean = FilterRegistrationBean(NakedDomainFilter(envConfig))
        registrationBean.order = Ordered.HIGHEST_PRECEDENCE + 90 // before ReverseProxyFilter
        return registrationBean
    }

    @Bean
    fun securityFilterRegistrationBean(envConfig: EnvConfig?): FilterRegistrationBean<SecurityFilter> {
        val registrationBean = FilterRegistrationBean(SecurityFilter(envConfig))
        registrationBean.order = Ordered.HIGHEST_PRECEDENCE + 80 // before nakedDomainFilter
        return registrationBean
    }

    @Bean
    fun faviconFilterRegistrationBean(): FilterRegistrationBean<FaviconFilter> {
        val registrationBean = FilterRegistrationBean(FaviconFilter(assetLoader.faviconFile))
        registrationBean.order = Ordered.HIGHEST_PRECEDENCE + 75 // before securityFilter
        return registrationBean
    }

    @Bean
    fun healthCheckFilterRegistrationBean(): FilterRegistrationBean<HealthCheckFilter> {
        val registrationBean = FilterRegistrationBean(HealthCheckFilter())
        registrationBean.order = Ordered.HIGHEST_PRECEDENCE + 70 // before faviconFilter
        return registrationBean
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayReverseProxyFilter(
            extractor: RequestDataExtractor?,
            mappingsProvider: MappingsProvider?,
            requestForwarder: RequestForwarder?,
            traceInterceptor: ProxyingTraceInterceptor?,
            requestInterceptor: PreForwardRequestInterceptor?
    ): ReverseProxyFilter {
        return ReverseProxyFilter(faradayProperties, extractor, mappingsProvider,
                requestForwarder, traceInterceptor, requestInterceptor)
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayHttpClientProvider(): HttpClientProvider {
        return HttpClientProvider()
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayRequestDataExtractor(): RequestDataExtractor {
        return RequestDataExtractor()
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayConfigurationMappingsProvider(envConfig: EnvConfig?,
                                             mappingsValidator: MappingsValidator?,
                                             httpClientProvider: HttpClientProvider?): MappingsProvider {
        return if (faradayProperties.isEnableProgrammaticMapping) {
            ProgrammaticMappingsProvider(
                    envConfig, serverProperties,
                    faradayProperties, mappingsValidator,
                    httpClientProvider)
        } else {
            ConfigurationMappingsProvider(
                    serverProperties,
                    faradayProperties, mappingsValidator,
                    httpClientProvider)
        }
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayLoadBalancer(): LoadBalancer {
        return RandomLoadBalancer()
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayMappingsValidator(): MappingsValidator {
        return MappingsValidator()
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayRequestForwarder(
            httpClientProvider: HttpClientProvider?,
            mappingsProvider: MappingsProvider?,
            loadBalancer: LoadBalancer?,
            meterRegistry: Optional<MeterRegistry?>?,
            traceInterceptor: ProxyingTraceInterceptor?,
            responseInterceptor: PostForwardResponseInterceptor?
    ): RequestForwarder {
        return RequestForwarder(
                serverProperties, faradayProperties, httpClientProvider,
                mappingsProvider, loadBalancer, meterRegistry,
                traceInterceptor, responseInterceptor)
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayTraceInterceptor(): TraceInterceptor {
        return LoggingTraceInterceptor()
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayProxyingTraceInterceptor(traceInterceptor: TraceInterceptor?): ProxyingTraceInterceptor {
        return ProxyingTraceInterceptor(faradayProperties, traceInterceptor)
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayPreForwardRequestInterceptor(envConfig: EnvConfig?): PreForwardRequestInterceptor {
        //return new NoOpPreForwardRequestInterceptor();
        return AuthRequestInterceptor(staffjoyPropreties.getSigningSecret(), envConfig)
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayPostForwardResponseInterceptor(): PostForwardResponseInterceptor {
        //return new NoOpPostForwardResponseInterceptor();
        return CacheResponseInterceptor()
    }

}