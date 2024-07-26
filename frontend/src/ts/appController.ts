/**
 * @license
 * Copyright (c) 2014, 2023, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
import * as ko from "knockout";
import "ojs/ojdrawerpopup";
import "ojs/ojknockout";
import "ojs/ojmodule-element";
import { ojNavigationList } from "ojs/ojnavigationlist";
import * as ResponsiveKnockoutUtils from "ojs/ojresponsiveknockoututils";
import * as ResponsiveUtils from "ojs/ojresponsiveutils";
import CoreRouter = require("ojs/ojcorerouter");
import ModuleRouterAdapter = require("ojs/ojmodulerouter-adapter");
import KnockoutRouterAdapter = require("ojs/ojknockoutrouteradapter");
import UrlParamAdapter = require("ojs/ojurlparamadapter");
import ArrayDataProvider = require("ojs/ojarraydataprovider");
import Context = require("ojs/ojcontext");
interface CoreRouterDetail {
  label: string;
  iconClass: string;
}

class RootViewModel {
  manner: ko.Observable<string>;
  message: ko.Observable<string | undefined>;
  smScreen: ko.Observable<boolean> | undefined;
  mdScreen: ko.Observable<boolean> | undefined;
  router: CoreRouter<CoreRouterDetail> | undefined;
  moduleAdapter: ModuleRouterAdapter<CoreRouterDetail>;
  sideDrawerOn: ko.Observable<boolean>;
  navDataProvider: ojNavigationList<
    string,
    CoreRouter.CoreRouterState<CoreRouterDetail>
  >["data"];
  appName: ko.Observable<string>;
  userLogin: ko.Observable<string>;
  selection: KnockoutRouterAdapter<CoreRouterDetail>;

  constructor() {
    // handle announcements sent when pages change, for Accessibility.
    this.manner = ko.observable("polite");
    this.message = ko.observable();
    let globalBodyElement: HTMLElement = document.getElementById(
      "globalBody"
    ) as HTMLElement;
    globalBodyElement.addEventListener(
      "announce",
      this.announcementHandler,
      false
    );
    // media queries for repsonsive layouts
    let smQuery: string | null = ResponsiveUtils.getFrameworkQuery("sm-only");
    if (smQuery) {
      this.smScreen =
        ResponsiveKnockoutUtils.createMediaQueryObservable(smQuery);
    }
    let mdQuery: string | null = ResponsiveUtils.getFrameworkQuery("md-up");
    if (mdQuery) {
      this.mdScreen =
        ResponsiveKnockoutUtils.createMediaQueryObservable(mdQuery);
    }

    const email = sessionStorage.getItem("usuario");
    const user = sessionStorage.getItem("user");
    const jsonObject = JSON.parse(user as string);
    console.log(user);

    let navData;
    if (!email) {
      navData = [
        { path: "", redirect: "login" },
        {
          path: "login",
          detail: {
            label: "",
            iconClass: "",
          },
        },
      ];
    } else {
      navData = [
        
        { path: "", redirect: "boardsList" },
        {
          path: "boards",
          detail: { label: "", iconClass: "" },
         
        },
        {
          path: "boardsList",
          detail: {
            label: "Lista de tableros",
            iconClass: "oj-ux-ico-user-configuration",
          },
        },
        {
          path: "profile",
          detail: {
            label: "Mi perfil",
            iconClass: "oj-ux-ico-user-configuration",
          },
        },
        
      ];
      // Comprueba si es admin
      if (jsonObject.roleId == "1") {
        navData.push({
          path: "admin",
          detail: {
            label: "Usuarios",
            iconClass: "oj-ux-ico-user-configuration",
          },
        });
      }
    }

    // navData;
    // router setup
    const router = new CoreRouter(navData, {
      urlAdapter: new UrlParamAdapter(),
    });
    router.sync();

    // Agregar configuración para la ruta "admin"
    // if (jsonObject.roleId == "1") {
    //   router.reconfigure({
    //     admin: {
    //       label: "Admin",
    //       isDefault: false,
    //       enter: (router, data) => {
    //         // Renderizar la página de administración
    //         // Aquí debes renderizar la página de administración correspondiente
    //       },
    //     },
    //   });
    // }

    this.moduleAdapter = new ModuleRouterAdapter(router);
    this.selection = new KnockoutRouterAdapter(router);
    // Setup the navDataProvider with the routes, excluding the first redirected
    // route.
    this.navDataProvider = new ArrayDataProvider(navData.slice(1), {
      keyAttributes: "path",
    });
    // drawer
    this.sideDrawerOn = ko.observable(false);
    // close drawer on medium and larger screens
    this.mdScreen?.subscribe(() => {
      this.sideDrawerOn(false);
    });
    // header
    // application Name used in Branding Area
    this.appName = ko.observable("Hive-5");
    // user Info used in Global Navigation area
    this.userLogin = ko.observable("");
    // release the application bootstrap busy state
    Context.getPageContext().getBusyContext().applicationBootstrapComplete();
  }

  announcementHandler = (event: any): void => {
    this.message(event.detail.message);
    this.manner(event.detail.manner);
  };
  // called by navigation drawer toggle button and after selection of nav drawer item
  toggleDrawer = (): void => {
    this.sideDrawerOn(!this.sideDrawerOn());
  };
  // a close listener so we can move focus back to the toggle button when the drawer closes
  openedChangedHandler = (event: CustomEvent): void => {
    if (event.detail.value === false) {
      const drawerToggleButtonElement = document.querySelector(
        "#drawerToggleButton"
      ) as HTMLElement;
      drawerToggleButtonElement.focus();
    }
  };
}
export default new RootViewModel();
