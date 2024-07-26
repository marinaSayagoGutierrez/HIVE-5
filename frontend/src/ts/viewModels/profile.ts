/**
 * @license
 * Copyright (c) 2014, 2024, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */

import ko = require("knockout");
import "ojs/ojbutton";
import "ojs/ojformlayout";
import "ojs/ojinputtext";
import "ojs/ojknockout";
import "ojs/ojlabel";
import { Collection, Model } from "ojs/ojmodel";

type User = {
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  password: string;
  role: Role;
  boards: {
    board: Board[];
  };
};

type Role = {
  id: number;
  name: string;
};

type Board = {
  id: number;
  name: string;
}

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

class ProfileViewModel {
  // Observables para los User
  private UserCol: ko.Observable = ko.observable();
  user: ko.Observable<User>;

  // Conseguir el userID del usuario iniciado la sessión
  userID: string;

  updated: ko.Observable = ko.observable(false);

  constructor() {
    const ssUser = sessionStorage.getItem("user");
    
    const jsonObject = JSON.parse(ssUser as string);
    this.userID = jsonObject.userId;
    this.user = ko.observable({
      userId: jsonObject.userId,
      email: jsonObject.email,
      firstName: jsonObject.firstName,
      lastName: jsonObject.lastName,
      password: jsonObject.password,
      role: {
        id: jsonObject.roleId,
        name: jsonObject.roleName
      },
      boards: jsonObject.boards
    });

    this.UserCol(this.createCollectionUser());
    this.UserCol()
      .fetch()
      .then(() => {
        this.getUser();
      })
      .catch((error: Error) => {
        console.error("Error al cargar la colección:", error);
      });
  }

  createCollectionUser = () => {
    const parseSaveUser = (response: User) => {
      return {
        ...response,
      };
    };

    const parseUser = (response: User) => {
      return {
        ...response,
      };
    };

    const restServerURLUsers = "http://localhost:8081/users";

    let UserCollection = Collection.extend({
      url: restServerURLUsers,
      model: Model.extend({
        urlRoot: restServerURLUsers,
        parse: parseUser,
        parseSave: parseSaveUser,
        idAttribute: "id",
      }),
      comparator: "id",
    });

    return new UserCollection();
  };

  getUser = () => {
    let model = this.UserCol().get(this.userID);

    let boardsData = model.get("boards");

    // Mapear los tableros si existen
    let boards = boardsData ? boardsData.board.map((board: any) => ({
      id: board.id,
      name: board.name
    })) : [];



    const userAux = {
        userId: model.get("id"),
        email: model.get("email"),
        firstName: model.get("firstName"),
        lastName: model.get("lastName"),
        password: model.get("password"),
        role: {
          id: model.get("role").id,
          name: model.get("role").name,
        },
        boards: {
          board: boards
        }
      };

    this.user(userAux); 

  };

  // Crear la colección de las tarjetas

  update = () => {
    this.updated(true);
  };

  submit = () => {
    let model = this.UserCol().get(this.userID);

    const user = {
     userId: this.user().userId,
     email: this.user().email,
     firstName: this.user().firstName,
     lastName: this.user().lastName,
     roleId: this.user().role.id,
     boards: this.user().boards ? {
      boardId: this.user().boards.board.map((board: any) => board.id)
    } : null
    };
    

    model.save(user, {
      success: () => {
        showLogAlert("El usuario ha sido actualizado con éxito", "#1D4355");
      },
      error: (textStatus: string) => {
        showLogAlert(
          "Error en la actualiación del usuario. Vuelvalo a intentar.",
          "#f44336"
        );
      },
      contentType: "application/json; charset=UTF-8",
      wait: true,
    });

  //   const jsonObject = {
  //     userId: user.userId,
  //     firstName: user.firstName,
  //     lastName: user.lastName,
  //     email: user.email,
  //     roleId: user.role.id,
  //     roleName: user.role.name,
  //     boards: user.boards ? {
  //         boardId: user.boards.board.map(board => board.id)
  //     } : null
  // };

  // Convertir el objeto JavaScript a una cadena JSON
    const jsonString = JSON.stringify(this.user());

    sessionStorage.setItem("user",jsonString);
    this.updated(false);
  };

  logout = () => {
    sessionStorage.clear();
    window.location.href = "http://localhost:8000";
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
  transitionCompleted(): void {}
}

export = ProfileViewModel;
