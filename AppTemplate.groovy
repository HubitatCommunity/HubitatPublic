/*
 * Import URL: https://raw.githubusercontent.com/HubitatCommunity/??/master/??-Driver.groovy"
 *
 *	Copyright 2019 your Name
 *
 *	Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *	use this file except in compliance with the License. You may obtain a copy
 *	of the License at:
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *	WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *	License for the specific language governing permissions and limitations
 *	under the License.
 *
 *
 */
	public static String version()      {  return "v???"  }

definition(
	name: "New App Template",
	namespace: "yourname",
	author: "your Name",
	description: "",
	iconUrl: "",
	iconX2Url: "",
	iconX3Url: ""
)

preferences 
{
   page(name: "mainPage")
}

/*
	mainPage
    
	Purpose: Displays the main (landing) page.

	Notes: 	Not very exciting.
*/
def mainPage() 
{
	dynamicPage(name: "", title: "", install: true, uninstall: true, refreshInterval:0) 
	{
		section("<h2>${app.label}</h2>"){}
		if (state?.serverInstalled == null || state.serverInstalled == false)
		{
			section("<b style=\"color:green\">${app.label} Installed!</b>")
			{
				paragraph "Click <i>Done</i> to save then return to ${app.label} to continue."
			}
			return
		}
		section(getFormat("title", " ${app.label}")) {}
		section("") 
		{
			input "pickedThis", "capability.actuator", title: "Select from Actuators", multiple: true, required: true
			input(name: "numberOption", type: "number", defaultValue: "10", range: "1..*", title: "", description: "", required: true)
		}
   		section("") 
   		{
   			input "logEnable", "bool", title: "Enable Debug Logging?", required: false, defaultValue: true
		}       
      	display()
	}
	
}


/*
	installed
    
	Purpose: Standard install function.

	Notes: Doesn't do much.
*/
def installed() 
{
	log.info "installed"
	state.serverInstalled = true
	initialize()
}


/*
	updated
    
	Purpose: Standard update function.

	Notes: Still doesn't do much.
*/
def updated() 
{
	log.info "updated run"
	unschedule()
      if (debugOutput) runIn(1800,logsOff) //disable debug logs after 30 min
	initialize()
}


/*
	initialize
    
	Purpose: Initialize the server instance.

	Notes: Does it all
*/
def initialize() 
{
	log.info "initialize"
	if (logEnable) log.debug "Debugs are: " + logEnable
	unschedule()
	//runEvery5Minutes(checkDevices)
}


/*
	uninstalled
    
	Purpose: uninstall the app.

	Notes: Does it all
*/
def uninstalled() 
{
	log.info "uninstalled"
}


/*
	display
    
	Purpose: Displays the title/copyright/version info

	Notes: 	Not very exciting.
*/
def display() {
	section{
	   paragraph getFormat("line")
		paragraph "<div style='color:#1A77C9;text-align:center;font-weight:small;font-size:9px'>Developed by: C Steele<br/>Version Status: $state.Status<br>Current Version: ${version()} -  ${thisCopyright}</div>"
    }
}

/*
	getFormat
    
	Purpose: Formats of the title/copyright/version info for consistency

	Notes: 	Not very exciting.
*/
def getFormat(type, myText=""){
	if(type == "header-green") return "<div style='color:#ffffff;font-weight: bold;background-color:#81BC00;border: 1px solid;box-shadow: 2px 3px #A9A9A9'>${myText}</div>"
	if(type == "line") return "\n<hr style='background-color:#1A77C9; height: 1px; border: 0;'></hr>"
	if(type == "title") return "<h2 style='color:#1A77C9;font-weight: bold'>${myText}</h2>"
}


/*
	logsOff
    
	Purpose: Disable debug logs

	Notes: 	Not very exciting. Called by a 'runin' timer, typically 30 min.
*/
def logsOff()
{
	log.warn "debug logging disabled..."
	app?.updateSetting("logEnable",[value:"false",type:"bool"])
}


def getThisCopyright(){"&copy; 2019 your Name"}

/**
mike.maxwell (Leader)
May 25, 2018

installed()  - runs when driver is installed, via pair or virtual
configure()  - runs when driver is installed, after installed is run. if capability Configuration exists, a Configure command is added to the ui
initialize() - runs first time driver loads, ie system startup when capability Initialize exists, a Initialize command is added to the ui.
updated()    - runs when save is clicked in the preferences section

Initialized() is not a capability or built in driver command, it's a local method only, I've never understood it either, i guess it saves a few lines of duplicate calls...
**/
