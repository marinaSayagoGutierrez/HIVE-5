import "ojs/ojchart";
import "ojs/ojinputtext";
import "ojs/ojlabel";
import "ojs/ojselectsingle";
import * as publicKey from "text!../publicKey.json";
import AsyncLengthValidator = require("ojs/ojasyncvalidator-length");
import ko = require("knockout");

function showLogAlert(message: string, backgroundColor: string) {
  const alertDiv = document.createElement("div");
  alertDiv.className = "custom-log-alert";
  alertDiv.textContent = message;
  alertDiv.style.backgroundColor = backgroundColor;
  document.body.appendChild(alertDiv);

  setTimeout(() => {
    if (alertDiv.parentNode) {
      alertDiv.parentNode.removeChild(alertDiv);
    }
  }, 2000);
}

type User = {
  email: string;
  password: string;
};

class LoginViewModel {
  lengthValue1: ko.Observable<string>;
  validators: ko.ObservableArray<AsyncLengthValidator<string>>;
  user: ko.Observable<User>;
  publicKey = JSON.parse(publicKey);

  constructor() {
    this.lengthValue1 = ko.observable("");
    this.validators = ko.observableArray([
      new AsyncLengthValidator({ min: 10, max: 50 }),
    ]);

    this.user = ko.observable(this.getEmptyItem());
  }

  public getEmptyItem = () => ({
    email: "",
    password: "",
  });

  login = async () => {
    const base64Credentials = btoa(
      `${this.user().email}:${this.user().password}`
    );

    const headers = new Headers({
      Authorization: `Basic ${base64Credentials}`,
      "Content-Type": "application/json",
    });

    const user = {
      email: this.user().email,
      pass: this.user().password,
    };
    
    try {
      sessionStorage.removeItem("usuario");

      const response = await fetch("http://localhost:8091/login", {
        method: "POST",
        headers: headers,
        body: JSON.stringify(user),
        credentials: "include",
        mode: "cors",
      });

      if (response.status == 200) {
        sessionStorage.setItem("usuario", JSON.stringify(user.email));

        const cookies = document.cookie.split(";");
        const oauthToken = cookies.find((cookie) =>
          cookie.trim().startsWith("oauthToken=")
        );

        if (oauthToken) {
          const tokenValue = oauthToken.trim().split("=")[1];
          document.cookie = `publicKey=${this.publicKey[0].publicKey}; path=/`;

          const headers2 = new Headers({
            "Content-Type": "application/json",
            Authorization: `Bearer ${tokenValue}`,
          });

          // Añadir cookies manualmente al header si es necesario
          headers2.append("Cookie", `publicKey=${this.publicKey}`);
          headers2.append("Cookie", `oauthToken=${oauthToken}`);

          const response2 = await fetch("http://localhost:8091/getToken", {
            method: "GET",
            headers: headers2,
            credentials: "include",
            mode: "cors",
          });

          const response2Body = await response2.text();

          if (response2.status == 200) {
            const response2BodyJSON = JSON.parse(response2Body);
            const user = response2BodyJSON.user;
            sessionStorage.setItem("user", user);

            return true;
          } else {
            console.error("Token fetch failed:", response2.status);
            showLogAlert(
              "Error obteniendo el token. Inténtelo de nuevo.",
              "#f44336"
            );
          }
        } else {
          showLogAlert(
            "No se encontró el token de OAuth. Inténtelo de nuevo.",
            "#f44336"
          );
        }
      } else {
        console.error("Login failed:", response.status);
        showLogAlert(
          "Las credenciales introducidas son incorrectas. Inténtelo de nuevo.",
          "#f44336"
        );
        return false;
      }
    } catch (error) {
      console.error("Error:", error);
      showLogAlert(
        "Error en el inicio de sesión. Inténtelo de nuevo.",
        "#f44336"
      );
      return false;
    }
  };

  acceder = async () => {
    const loggedUser = await this.login();
    setTimeout(() => {
      if (loggedUser) {
        window.location.href = "http://localhost:8000/?ojr=boardsList";
      }
    }, 400);
  };

  /**
   * Optional ViewModel method invoked after the View is inserted into the
   * document DOM.  The application can put logic that requires the DOM being
   * attached here.
   * This method might be called multiple times - after the View is created
   * and inserted into the DOM and after the View is reconnected
   * after being disconnected.
   */
  connected(): void {
    // implement further logic if needed
  }

  /**
   * Optional ViewModel method invoked after the View is disconnected from the DOM.
   */
  disconnected(): void {
    // implement if needed
  }

  /**
   * Optional ViewModel method invoked after transition to the new View is complete.
   * That includes any possible animation between the old and the new View.
   */
  transitionCompleted(): void {
    // implement if needed
  }
}

export = LoginViewModel;
