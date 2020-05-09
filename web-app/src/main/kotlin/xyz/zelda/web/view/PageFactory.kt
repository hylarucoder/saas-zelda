package xyz.zelda.web.view

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import xyz.zelda.infra.config.StaffjoyProps
import xyz.zelda.web.props.AppProps

@Component
class PageFactory {
    @Autowired
    var StaffjoyProps: StaffjoyProps? = null

    @Autowired
    var appProps: AppProps? = null
    fun buildHomePage(): Page {
        return Page.builder()
                .title("Staffjoy - Online Scheduling Software")
                .description("Staffjoy is a web application that helps small businesses create schedules online and automatically communicate them via text message with hourly workers.")
                .templateName("home")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildAboutPage(): Page {
        return Page.builder()
                .title("About Staffjoy")
                .description("Learn about the members of the Staffjoy team and the origin of the company.")
                .templateName("about")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildCareersPage(): Page {
        return Page.builder()
                .title("Staffjoy Careers")
                .description("If you’re looking to improve the way small businesses schedule their hourly workers, you are invited to apply to join our team in San Francisco.")
                .templateName("careers")
                .cssId("careers")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildPricingPage(): Page {
        return Page.builder()
                .title("Staffjoy Pricing")
                .description("Staffjoy’s software pricing is affordable for any size team. There is a monthly subscription based on the number of employees your company has.")
                .templateName("pricing")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildPrivacyPolicyPage(): Page {
        return Page.builder()
                .title("Staffjoy Privacy Policy")
                .description("Staffjoy’s Privacy Policy will walk you through through security protocols, data storage, and legal compliance that all clients need to know.")
                .templateName("privacypolicy")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildSignupPage(): Page {
        return Page.builder()
                .title("Staffjoy Privacy Policy")
                .description("Sign Up for Your 30 Day Free Staffjoy Trial\", Description: \"Sign up for a 30 day free trial of Staffjoy today to create your schedule online. We’ll distribute it to your team using automated text messages.")
                .templateName("signup")
                .cssId("sign-up")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildEarlyPage(): Page {
        return Page.builder()
                .title("Early Access Signup")
                .description("Get early access for Staffjoy")
                .templateName("early")
                .cssId("sign-up")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildTermsPage(): Page {
        return Page.builder()
                .title("Staffjoy Terms and Conditions")
                .description("Staffjoy’s Terms and Conditions point out the liability, disclaimers, exclusions, and more that all users of our website must agree to.")
                .templateName("terms")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildConfirmPage(): Page {
        return Page.builder()
                .title("Open your email and click on the confirmation link!")
                .description("Check your email and click the link for next steps")
                .templateName("confirm")
                .cssId("confirm")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildNewCompanyPage(): Page {
        return Page.builder()
                .title("Create a new company")
                .description("Get started with a new Staffjoy account")
                .templateName("new_company")
                .cssId("newCompany")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    // lombok inheritance workaround, details here: https://www.baeldung.com/lombok-builder-inheritance
    fun buildLoginPage(): LoginPage {
        return LoginPage.childBuilder()
                .title("Staffjoy Log in")
                .description("Log in to Staffjoy to start scheduling your workers. All you’ll need is your email and password.")
                .templateName("login")
                .cssId("login")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildActivatePage(): ActivatePage {
        return ActivatePage.childBuilder()
                .title("Activate your Staffjoy account")
                .templateName("activate")
                .cssId("sign-up")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildResetPage(): ResetPage {
        return ResetPage.childBuilder()
                .title("Password Reset")
                .cssId("sign-up")
                .templateName("reset")
                .description("Reset the password for your Staffjoy account.")
                .recaptchaPublic(appProps.getRecaptchaPublic())
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildResetConfirmPage(): Page {
        return Page.builder()
                .title("Please check your email for a reset link!")
                .description("Check your email and click the link for next steps")
                .templateName("confirm")
                .cssId("confirm")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }

    fun buildConfirmResetPage(): ConfirmResetPage {
        return ConfirmResetPage.childBuilder()
                .title("Reset your Staffjoy password")
                .description("Follow steps to reset your Staffjoy password")
                .cssId("sign-up")
                .templateName("confirmreset")
                .version(StaffjoyProps.getDeployEnv())
                .build()
    }
}