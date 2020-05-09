package xyz.zelda.web.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import xyz.zelda.web.view.Constant
import xyz.zelda.web.view.PageFactory

@Controller
class StaticPageController {
    @Autowired
    private val pageFactory: PageFactory? = null

    @RequestMapping(value = "")
    fun getHome(model: Model): String {
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildHomePage())
        return Constant.VIEW_HOME
    }

    @RequestMapping(value = "/about")
    fun getAbout(model: Model): String {
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildAboutPage())
        return Constant.VIEW_ABOUT
    }

    @RequestMapping(value = "/careers")
    fun getCareers(model: Model): String {
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildCareersPage())
        return Constant.VIEW_CAREERS
    }

    @RequestMapping(value = "/pricing")
    fun getPricing(model: Model): String {
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildPricingPage())
        return Constant.VIEW_PRICING
    }

    @RequestMapping(value = "/privacy-policy")
    fun getPrivacyPolicy(model: Model): String {
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildPrivacyPolicyPage())
        return Constant.VIEW_PRIVACY_POLICY
    }

    @RequestMapping(value = "/sign-up")
    fun getSignup(model: Model): String {
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildSignupPage())
        return Constant.VIEW_SIGNUP
    }

    @RequestMapping(value = "/early-access")
    fun getEarlyAccess(model: Model): String {
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildEarlyPage())
        return Constant.VIEW_EARLY_ACCESS
    }

    @RequestMapping(value = "/terms")
    fun getTerms(model: Model): String {
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildTermsPage())
        return Constant.VIEW_TERMS
    }
}