/*
 * Copyright (c) Interactive Information R & D (I2RD) LLC.
 * All Rights Reserved.
 *
 * This software is confidential and proprietary information of
 * I2RD LLC ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered
 * into with I2RD.
 */

package net.venturetech.event_api.config.automation.site.profile.basic

import net.venturetech.event_api.config.ProjectInformation
import net.venturetech.event_api.login.social.ui.SocialLogin
import net.venturetech.event_api.profile.ui.ApplicationFunctions.*
import net.venturetech.event_api.support.service.AppUtil
import com.i2rd.cms.bean.ResetPasswordBean.AuthenticationMeans.EMAIL_ONLY
import com.i2rd.cms.visibility.AuthenticationCondition
import experimental.cms.dsl.AppDefinition
import experimental.cms.dsl.AppFunctionPage
import experimental.cms.dsl.content.*
import net.proteusframework.cms.component.page.PageProperties.Type.page
import net.proteusframework.cms.component.page.PageProperties.Type.page_template
import net.proteusframework.cms.component.page.layout.BoxDescriptor.*
import net.proteusframework.cms.login.oneall.service.OneAllLoginService
import net.proteusframework.cms.login.social.ui.SocialLoginMode
import net.proteusframework.email.config.ContentElementEmailConfigType
import net.proteusframework.users.model.AuthenticationMethodSecurityLevel.SHARED_IDENTIFIER
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.net.URL

private val appName = ProjectInformation.APPLICATION_NAME


@Profile("automation")
@Component
open class ProfileBasicDSL : AppDefinition(DEFINITION_NAME, version = 1, siteId = SITE_FRONTEND, init = {
    libraryResources(URL("https://repo.venturetech.net/artifactory/vt-snapshot-local/" +
        "net/venturetech/event-api/\${LATEST}/event-api-\${LATEST}-libraries.zip"))
    webResources(URL("https://repo.venturetech.net/artifactory/simple/vt-snapshot-local/" +
        "net/venturetech/event-api-webdev/\${LATEST}/event-api-webdev-\${LATEST}.zip"))

    val frontendRoleProgId = "frontend_user"
    val adminRoleProgId = "proteus_admin"
    role("Frontend User", frontendRoleProgId, "Frontend User", 4)
    role("Proteus Admin", adminRoleProgId, "Proteus Admin", 4)

    storeSitePreference(AppUtil.PREF_KEY_PROFILE_SITE)
    systemPref(AppUtil.PREF_KEY_FRONTEND_ROLE, frontendRoleProgId)
    systemPref(AppUtil.PREF_KEY_ADMIN_ROLE, adminRoleProgId)
    meta("viewport", "width=device-width, initial-scale=1.0, minimal-u")
    meta("apple-mobile-web-app-capable", "yes")

    template("Login") {
        javaScript("vendor/jquery.min.js")
        javaScript("vendor/tether.min.js")
        javaScript("vendor/bootstrap.min.js")
        javaScript("vendor/select2.min.js")
        javaScript("templates/main.min.js")
        css("vendor/select2.min.css")
        css("templates/template--base.min.css")
        layout("Header, Body, Footer") {
            box("Enclosing") {
                htmlId = "e-content"
                htmlClass = "enclosing"
                box("Header") {
                    htmlId = "header"
                    defaultContentArea = page_template
                    boxType = HEADER
                }
                box("Body Wrapper") {
                    htmlId = "body-wrapper"
                    box("Body") {
                        htmlId = "body"
                        defaultContentArea = page
                        boxType = MAIN
                    }
                }
            }
            box("Footer") {
                htmlId = "footer"
                defaultContentArea = page_template
                boxType = FOOTER
            }
        }

        content("Header", HTML("Logo")){
            htmlContent = """<a href="/"><img src="application-logo.png" alt="logo" class="logo"/></a>"""
            htmlClass = "logo-con"
        }
        content("Header", HTML("Logo & Menu Toggle")){
            htmlContent = """< a href="/"><img src="application-logo.png" alt="logo" class="logo"/>
</a><div class="fa fa-bars"></div>"""
            htmlClass = "logo-con"
        }.remove()

        content("Footer", Text("Global Footer")){
            htmlContent = """<p>$appName | &copy; 2016</p>
<p><em>Powered By:</em></p>
<a href="http://www.venturetech.net/platform" target="_blank"><img alt="Proteus Platform" src="proteus-logo.svg"/></a>
"""
        }
    }

    template("Frontend") {
        javaScript("vendor/jquery.min.js")
        javaScript("vendor/tether.min.js")
        javaScript("vendor/bootstrap.min.js")
        javaScript("vendor/select2.min.js")
        javaScript("templates/main.min.js")
        css("vendor/select2.min.css")
        css("templates/template--base.min.css")
        css("templates/font-awesome.min.css")
        layout("Header, Body, Footer")

        content("Header", "Logo")
        content("Header", HTML("Hamburger")){
            htmlContent = """<div class="hamburger">
  <span class="bar top"></span>
  <span class="bar middle"></span>
  <span class="bar bottom"></span>
</div>"""
            htmlClass = "hamburger-con"
        }
        content("Header", HTML("Logo & Menu Toggle")).remove()

        content("Header", ScriptedGenerator("Company Selector")) {
            script("CompanySelectorScript.groovy")
        }
        content("Header", Composite("Nav/Profile container")) {
            htmlClass = "nav-profile-con"
            content(ScriptedGenerator("Top Menu")) {
                script("AppFunctionMenuScript.groovy")
            }
            content(Logout("Logout")) {
                htmlClass = "menu"
                htmlId = "logout"

            }
        }
        content("Footer", "Global Footer")
    }

    page("Login", "/login") {
        template("Login")
        css("pages/page--login.min.css")
        content("Body", Composite("Login Group")) {
            content(Text("Already Logged-In Text")) {
                htmlContent = "<p>You're logged in. Click <a href=\"/dashboard\">HERE</a> to return to $appName.</p>"
                htmlClass = "logged-in"
                val authenticationCondition = AuthenticationCondition()
                val dataMap = authenticationCondition.configurationDataMap
                dataMap[AuthenticationCondition.FIELD_SECURITY_LEVEL] = SHARED_IDENTIFIER
                dataMap.update()
                visibilityCondition = authenticationCondition
            }
            content(Login("Login")) {
                htmlClass = "form-signin"
                titleText = "<p>Sign In Now</p>"
                forgotPasswordText = """<p><a href="/password-reset">Forgot your password?</a></p>"""
                landingPage("Dashboard")
                scriptedRedirect("ScriptableRedirect/StarterSiteRedirectScript.groovy")
                scriptedRedirectParam("Default Redirect Page", "/dashboard")
            }
            content(SocialLogin("Social Login")) {
                htmlClass = "oneall-social-login"
                landingPage("Dashboard")
                loginService(OneAllLoginService.SERVICE_IDENTIFIER)
                provider("google")
                mode(SocialLoginMode.Login)
                additionalProperty(OneAllLoginService.PROP_SSO_ENABLED, "true")
            }
        }
    }

    emailTemplate(ContentElementEmailConfigType::class.java, "Reset Password", "default-reset-password") {
        to = "\${recipient.findEmailAddress()}"
        from = "\"$appName\" <noreply@venturetech.net>"
        replyTo = from
        subject = "$appName: Reset Your  Password"
        htmlContent = """<p>Dear ${"$"}{(recipient.name.first)!''} ${"$"}{(recipient.name.last)!''},</p>

<p>To reset your password for $appName, please <a href="${"$"}{action_url!validation_default}">click here</a>.</p>

<p>$appName</p>
<a href="/dashboard"><img src="application-logo.png" alt="logo" class="logo"/></a>
"""
    }

    page("Password Reset", "/password-reset") {
        template("Login")
        css("pages/page--login.min.css")
        content("Body", Composite("Reset Password Group")) {
            htmlClass = "retrieve-password-con"
            content(HTML("Reset Password Instructions")) {
                htmlContent = """<div class="title">Reset Password</div>
<p >To reset your password, please provide the e-mail address stored for your user account. If the e-mail address
match an existing user account, then an email message will be sent with additional instructions.</p>"""
            }
            content(ResetPassword("Reset Password")){
                authenticationMeans = EMAIL_ONLY
                loginPage("Login")
                ignoreDynamicReturnPath = true
            }
        }
    }

    page("Dashboard", "/dashboard") {
        template("Frontend")
        permission("Frontend Access", adminRoleProgId)
        authenticationPage("Login")
        content("Body", Text("Dashboard Heading")) {
            htmlContent = "<h1>Dashboard</h1>"
        }
    }

    for((appFunction, path, htmlClassName, cssPaths) in listOf(
        AppFunctionPage(Company.MANAGEMENT, "/\${folder.company}/manage",
            "company-management"),
        AppFunctionPage(Company.VIEW, "/\${folder.company}/view/*", "company-viewer"),
        AppFunctionPage(Company.EDIT, "/\${folder.company}/edit/*", "company-editor"),
        AppFunctionPage(StarterSite.SETUP, "/site/setup", "site-setup"),
        AppFunctionPage(User.MANAGEMENT, "/user/manage", "user-management"),
        AppFunctionPage(User.VIEW, "/user/view/*", "user-viewer"),
        AppFunctionPage(User.EDIT, "/user/edit/*", "user-editor"),
        AppFunctionPage(User.MY_ACCOUNT_VIEW, "/account/my-profile", "my-profile", listOf("page--user-profile.min.css")),
        AppFunctionPage(User.MY_ACCOUNT_EDIT, "/account/my-profile/edit/*", "my-profile-edit",
            listOf("page--user-profile.min.css"))
     )) {
        page(appFunction, path) {
            template("Frontend")
            permission("Frontend Access", frontendRoleProgId)
            authenticationPage("Login")
            cssPaths.forEach { css(it) }
            content("Body", ApplicationFunction(appFunction)) {
                htmlClass = htmlClassName
            }
            if(appFunction == User.MY_ACCOUNT_VIEW) {
                content("Body", SocialLogin("OneAll Link")) {
                    htmlClass = "oneall-link"
                    loginService(OneAllLoginService.SERVICE_IDENTIFIER)
                    provider("google")
                    mode(SocialLoginMode.Link)
                }
            }
        }
    }

    hostname("\${proteus.install.name}.\${base.domain}", "Dashboard")

    content(FileServer("Sourcemaps")){
        directory = "Sourcemaps"
        path = "_sourcemaps/*"
    }

    content(FileServer("Design")){
        directory = "Design"
        path = "_design/*"
    }
}) {
    companion object {
        internal const val DEFINITION_NAME = "Profile Basic"
        val SITE_FRONTEND = "$appName Frontend"
    }
}

