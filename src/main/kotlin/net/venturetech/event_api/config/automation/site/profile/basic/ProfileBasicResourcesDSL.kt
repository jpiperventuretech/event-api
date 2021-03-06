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

import net.venturetech.event_api.config.automation.site.profile.basic.ProfileBasicDSL.Companion.SITE_FRONTEND
import net.venturetech.event_api.profile.ui.ApplicationFunctions.Company.Resource
import experimental.cms.dsl.AppDefinition
import experimental.cms.dsl.AppFunctionPage
import experimental.cms.dsl.content.ApplicationFunction
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("automation")
@Component
open class ProfileBasicResourcesDSL :
    AppDefinition(DEFINITION_NAME, version = 1, siteId = SITE_FRONTEND, dependency = ProfileBasicDSL.DEFINITION_NAME, init = {

    for((appFunction, path, htmlClassName) in listOf(
        AppFunctionPage(Resource.MANAGEMENT, "/resource/manage", "resource-management"),
        AppFunctionPage(Resource.EDIT, "/resource/edit/*", "resource-editor"),
        AppFunctionPage(Resource.VIEW, "/resource/view/*", "resource-viewer")
    )) {

        page(appFunction, path) {
            template("Frontend")
            permission("Frontend Access")
            authenticationPage("Login")

            content("Body", ApplicationFunction(appFunction)) {
                htmlClass = htmlClassName
            }
        }
    }

}) {
    companion object {
        internal const val DEFINITION_NAME = "${ProfileBasicDSL.DEFINITION_NAME} - Resources"
    }
}
