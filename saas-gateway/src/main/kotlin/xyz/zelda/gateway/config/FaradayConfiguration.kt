package xyz.zelda.faraday.config

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.Ordered

import xyz.zelda.gateway.core.balancer.LoadBalancer
import xyz.zelda.gateway.core.balancer.RandomLoadBalancer
import xyz.zelda.gateway.core.filter.FaviconFilter
import xyz.zelda.gateway.core.filter.HealthCheckFilter
import xyz.zelda.gateway.core.filter.NakedDomainFilter
import xyz.zelda.gateway.core.filter.SecurityFilter
import xyz.zelda.gateway.core.http.HttpClientProvider
import xyz.zelda.gateway.core.http.RequestDataExtractor
import xyz.zelda.gateway.core.http.RequestForwarder
import xyz.zelda.gateway.core.http.ReverseProxyFilter
import xyz.zelda.gateway.core.interceptor.AuthRequestInterceptor
import xyz.zelda.gateway.core.interceptor.CacheResponseInterceptor
import xyz.zelda.gateway.core.interceptor.PostForwardResponseInterceptor
import xyz.zelda.gateway.core.interceptor.PreForwardRequestInterceptor
import xyz.zelda.gateway.core.mappings.ConfigurationMappingsProvider
import xyz.zelda.gateway.core.mappings.MappingsProvider
import xyz.zelda.gateway.core.mappings.MappingsValidator
import xyz.zelda.gateway.core.mappings.ProgrammaticMappingsProvider
import xyz.zelda.gateway.core.trace.LoggingTraceInterceptor
import xyz.zelda.gateway.core.trace.ProxyingTraceInterceptor
import xyz.zelda.gateway.core.trace.TraceInterceptor

import xyz.zelda.gateway.view.AssetLoader
import xyz.zelda.infra.config.ZeldaProperties
import xyz.zelda.infra.config.ZeldaWebConfig
import xyz.zelda.infra.env.EnvConfig

@Configuration
@EnableConfigurationProperties(FaradayProperties::class, ZeldaProperties::class)
@Import(value = [ZeldaWebConfig::class])
class FaradayConfiguration(
    protected val faradayProperties: FaradayProperties,
    protected val serverProperties: ServerProperties,
    protected val zeldaProperties: ZeldaProperties,
    protected val assetLoader: AssetLoader
) {
    @Bean
    fun faradayReverseProxyFilterRegistrationBean(
            proxyFilter: ReverseProxyFilter
    ): FilterRegistrationBean<ReverseProxyFilter> {
        val registrationBean = FilterRegistrationBean(proxyFilter)
            registrationBean.order = faradayProperties.filterOrder // by default to Ordered.HIGHEST_PRECEDENCE + 100
            return registrationBean
    }

    @Bean
    fun nakedDomainFilterRegistrationBean(envConfig: EnvConfig?): FilterRegistrationBean<NakedDomainFilter> {
        val registrationBean = FilterRegistrationBean(NakedDomainFilter(envConfig!!))
        registrationBean.order = Ordered.HIGHEST_PRECEDENCE + 90 // before ReverseProxyFilter
        return registrationBean
    }

    @Bean
    fun securityFilterRegistrationBean(envConfig: EnvConfig?): FilterRegistrationBean<SecurityFilter> {
        val registrationBean = FilterRegistrationBean(SecurityFilter(envConfig!!))
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
        extractor: RequestDataExtractor,
        mappingsProvider: MappingsProvider,
        requestForwarder: RequestForwarder,
        traceInterceptor: ProxyingTraceInterceptor,
        requestInterceptor: PreForwardRequestInterceptor
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
    fun faradayConfigurationMappingsProvider(
        envConfig: EnvConfig,
        mappingsValidator: MappingsValidator,
        httpClientProvider: HttpClientProvider
    ): MappingsProvider {
        return if (faradayProperties.enableProgrammaticMapping) {
            ProgrammaticMappingsProvider(
                envConfig,
                serverProperties,
                faradayProperties,
                mappingsValidator,
                httpClientProvider
            )
        } else {
            ConfigurationMappingsProvider(
                serverProperties,
                faradayProperties,
                mappingsValidator,
                httpClientProvider
            )
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
        httpClientProvider: HttpClientProvider,
        mappingsProvider: MappingsProvider,
        loadBalancer: LoadBalancer,
        meterRegistry: MeterRegistry,
        traceInterceptor: ProxyingTraceInterceptor,
        responseInterceptor: PostForwardResponseInterceptor
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
        return ProxyingTraceInterceptor(faradayProperties, traceInterceptor!!)
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayPreForwardRequestInterceptor(envConfig: EnvConfig?): PreForwardRequestInterceptor {
        //return new NoOpPreForwardRequestInterceptor();
        return AuthRequestInterceptor(zeldaProperties.signingSecret, envConfig!!)
    }

    @Bean
    @ConditionalOnMissingBean
    fun faradayPostForwardResponseInterceptor(): PostForwardResponseInterceptor {
        //return new NoOpPostForwardResponseInterceptor();
        return CacheResponseInterceptor()
    }

}