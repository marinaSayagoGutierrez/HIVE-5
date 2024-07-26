/**
 * @license
 * Copyright (c) 2014, 2024, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */

import { ojButtonEventMap } from "ojs/ojbutton";
import { ojButton } from 'ojs/ojbutton';
import "ojs/ojdialog";
import { ojDialog } from "ojs/ojdialog";
import "ojs/ojformlayout";
import "ojs/ojinputtext";
import { KeySetImpl } from "ojs/ojkeyset";
import { ObservableKeySet } from "ojs/ojknockout-keyset";
import "ojs/ojlistview";
import { ojListView } from "ojs/ojlistview";
import { Collection, Model } from "ojs/ojmodel";
import "ojs/ojselectcombobox";
import ko = require("knockout");
import CollectionDataProvider = require("ojs/ojcollectiondataprovider");
import 'ojs/ojbutton';
import "ojs/ojlabelvalue";
import "ojs/ojselectsingle";

import AsyncLengthValidator = require("ojs/ojasyncvalidator-length");
import ArrayDataProvider = require("ojs/ojarraydataprovider");
import Context = require("@oracle/oraclejet/ojcontext");

type Board = {
  id: number;
  name: string;
  users: User[];
};

type Role = {
    id: number;
    name: string;
  };
  
type User = {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
  boards: Board[]
};

type Status = {
  id: number;
  name: string;
};

type Card = {
  id: number;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  priority: number;
  board: Board;
  status: Status;
  user: User;
};

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

class BoardsListViewModel {
  // Conseguir el userID del usuario iniciado la sessión
  userID: string;
  ServerURLBoards= "http://localhost:8082/boards";
  ServerURLCards = "http://localhost:8080/cards";

  user: ko.Observable<User>;

  board: ko.Observable<Board>;
  boardCol: ko.Observable = ko.observable();
  boardSource: ko.Observable = ko.observable();

  cardCol: ko.Observable = ko.observable();

  sendBoardId: ko.Observable<number>;
  receivedBoardId: ko.Observable<number>;
  
  validatorTitle: ko.ObservableArray<AsyncLengthValidator<string>>;

  browsersBoard: any[];
  boardsDPSend: ko.Observable<any>;
  boardsDPReceive: ko.Observable<any>;
  
  constructor() {
    this.validatorTitle = ko.observableArray([
      new AsyncLengthValidator({ min: 4, max: 30}),
    ]);

    this.sendBoardId = ko.observable(0);
    this.receivedBoardId = ko.observable(0);

    const ssUser = sessionStorage.getItem("user");
    const jsonObject = JSON.parse(ssUser as string);
    this.userID = jsonObject.userId;

    this.user = ko.observable<User>({
        id: jsonObject.userId,
        email: jsonObject.email,
        firstName: jsonObject.firstName,
        lastName: jsonObject.lastName,
        role: {
          id: jsonObject.roleId,
          name: jsonObject.roleName,
        },
        boards: jsonObject.boards
      });

    this.board = ko.observable<Board>({
      id: 0,
      name: "",
      users: [] 
    });
    const boardColAux = this.createCollectionBoard();
    this.boardCol(boardColAux);

    const cardColAux = this.createCollectionCards();
    this.cardCol(cardColAux);

    
    
    if(this.user().role.id != 1) this.boardFilter()
    else this.boardSource(new CollectionDataProvider(this.boardCol()));

    this.browsersBoard = [];
    this.boardsDPSend = ko.observable(null);
    this.boardsDPReceive = ko.observable(null);

    this.fetchData();
    
  
  }

  
  private parseSaveBoard = (response: Board) => {
    return {
      ...response,
    };
  };

  private parseBoard = (response: Board) => {
    return {
      ...response,
    };
  };

  private parseSaveCard = (response: Card) => {
    return {
      ...response,
    };
  };

  private parseCard = (response: Card) => {
    return {
      ...response,
    };
  };


  public createCollectionBoard = () => {
    let MyBoardCollection = Collection.extend({
      url: this.ServerURLBoards,
      model: Model.extend({
        urlRoot: this.ServerURLBoards,
        parse: this.parseBoard,
        parseSave: this.parseSaveBoard,
        idAttribute: "id",
      }),
      comparator: "id",
    });
    return new MyBoardCollection();
  };

  public createCollectionCards = () => {
    let MyCardCollection = Collection.extend({
      url: this.ServerURLCards,
      model: Model.extend({
        urlRoot: this.ServerURLCards,
        parse: this.parseCard,
        parseSave: this.parseSaveCard,
        idAttribute: "id",
      }),
      comparator: "id",
    });
    return new MyCardCollection();
  };

  fetchData() {
    this.boardCol()
    .fetch()
      .then(() => {
        this.browsersBoard = this.boardCol().map((board: any) => {
          return { value: board.get("id"), label: board.get("name") };
        });

        this.boardsDPSend(new ArrayDataProvider(this.browsersBoard, {
          keyAttributes: "value"
        }));

        this.boardsDPReceive(new ArrayDataProvider(this.browsersBoard, {
          keyAttributes: "value"
        }));

      })
      .catch((error: Error) => {
        console.error("Error al cargar la colección:", error);
      });
  }

  boardChanged = (event: any) => {
    this.browsersBoard = this.boardCol().map((board: any) => {
      return { value: board.get("id"), label: board.get("name") };
    });
   if (this.sendBoardId() != null) {
    
      this.browsersBoard = this.browsersBoard.filter(board => board.value !== this.sendBoardId());
      this.boardsDPReceive(new ArrayDataProvider(this.browsersBoard, {
        keyAttributes: "value"
      }));
   } else {
    this.browsersBoard = this.browsersBoard.filter(board => board.value !== this.receivedBoardId());

    this.boardsDPSend(new ArrayDataProvider(this.browsersBoard, {
      keyAttributes: "value"
    }));
   }
  }

  public inicializar = () => {
    this.board({
      id: 0,
      name: "",
      users: [] 
    });
  };

  public boardFilter = () => {
    
    this.boardCol().fetch({
      success: (collection: Collection) => {
        const data = collection.toJSON() as Board[];
        
        // Crear colecciones filtradas
        const createFilteredCollection = (filteredData: Board[]) => {
          const modelArray = filteredData.map(
            (board) => new (this.boardCol().model)(board)
          );
          return new (Collection.extend({ model: this.boardCol().model }))(
            modelArray
          );
        };

        // Actualiza la colección inicial
        // this.boardCol().reset(data);

        // Asignar las colecciones filtradas a los observables
        this.boardCol(createFilteredCollection(this.user().boards));
        // Actualiza los Source con las colecciones ya filtradas
        this.boardSource(new CollectionDataProvider(this.boardCol()));
      },
      error: (collection: Collection, error: any) => {
        console.error("Error fetching data:", error);
      },
    });
  };

  public cardFilter = () => {
    this.cardCol().fetch({
      success: (collection: Collection) => {


        const createFilteredCollection = (filteredData: Card[]) => {
          const modelArray = filteredData.map(
            (card) => new (this.cardCol().model)(card)
          );
          
          return new (Collection.extend({ model: this.cardCol().model, url: this.ServerURLCards }))(
            modelArray
          );
        };

        const data = collection.toJSON() as Card[];

        

        const board = data.filter((card) => card.board.id == this.sendBoardId());
        
        const aux = createFilteredCollection(board);
        this.cardCol(aux);
        // this.cardCol().reset(board);

        
      },
      error: (collection: Collection, error: any) => {
        console.error("Error fetching data:", error);
      },
    });
  };


  public showCreateDialog = (event: ojButtonEventMap["ojAction"]) => {
    if (this.board().name != "") this.inicializar();
    (document.getElementById("createDialog") as ojDialog).open();
  };

  public showExportDialog = (event: ojButtonEventMap["ojAction"]) => {
    if(this.sendBoardId() !== 0) {this.sendBoardId(0)}
    if(this.receivedBoardId() !== 0) {this.receivedBoardId(0)}

    this.fetchData();


    (document.getElementById("exportDialog") as ojDialog).open();
  };

  public showEditDialog = (event: ojButton.ojAction,
    context: ojListView.ItemContext<Board['id'], Board>) => {
      
    const myModel = this.boardCol().get(context.key);
    const aux = {
      id: myModel.get("id"),
      name: myModel.get("name"),
      users: myModel.get("users"),
    }
    this.board(aux);

    (document.getElementById("editDialog") as ojDialog).open();
  };

  createBoard = async () => {
    const row = {
      ...this.board()
    };

    console.log("Datos que se van a enviar: ", JSON.stringify(row));

    this.boardCol().create(row, {
      method: "POST",
      wait: true,
      contentType: "application/json; charset=UTF-8",

      success: (model: Model, response: Response) => {
        console.log("Board created successfully:", response);
        showLogAlert("Tablero creada con éxito", "#1D4355");
        (document.getElementById("createDialog") as ojDialog).close();
      },
      error: (model: Model, response: Response) => {
        console.error("Failed to create board:", response);
        showLogAlert(
          "Error: faltan datos. Por favor, rellene todos los campos.",
          "#f44336"
        );
      },
    });

  };

  updateBoard = async () => {
    const myModel = this.boardCol().get(this.board().id);

    const row = {
      ...this.board()
    };

    myModel.save(row, {
      wait: true,
      contentType: "application/json; charset=UTF-8",

      success: (model: Model, response: Response) => {
        console.log("Board updated successfully:", response);
        showLogAlert("Tablero actualizada con éxito", "#1D4355");
        (document.getElementById("editDialog") as ojDialog).close();
      },
      error: (model: Model, response: Response) => {
        console.error("Failed to update board:", response);
        showLogAlert(
          "Error: faltan datos. Por favor, rellene todos los campos.",
          "#f44336"
        );
      },
    });
  };

  deleteBoard = async (event?: ojButton.ojAction,
    context?: ojListView.ItemContext<Board['id'], Board>, move?: boolean) => {
    let idDelete = this.sendBoardId()
    if(context) {
      idDelete = context.key
    }
    
    const myModel = this.boardCol().get(idDelete);
    if (myModel) {
      if (move === true) {
        this.boardCol().remove(myModel);
        myModel.destroy()
          .then(() => {
            showLogAlert("Tablero eliminada con éxito", "#1D4355");
          });
      } else {
        if (window.confirm("Estás seguro que deseas borrar el tablero?")) {
          this.boardCol().remove(myModel);
          myModel.destroy()
            .then(() => {
              showLogAlert("Tablero eliminada con éxito", "#1D4355");
            });
        }
      }
    }
  };




  changeBoard = async () => {

    this.cardCol().fetch().then(() => {
      this.cardFilter();
      this.cardCol()
      .fetch()
      .then(() => {
        this.cardCol().map((cardModel: any) => {
          const row = {
            cardId: cardModel.get("id"),
            title: cardModel.get("title"),
            description: cardModel.get("description"),
            startDate: cardModel.get("startDate"),
            endDate: cardModel.get("endDate"),
            priority: cardModel.get("priority"),
            boardId: this.receivedBoardId(),
            statusId: cardModel.get("status").id,
            userId: cardModel.get("user").id
          };

      
          cardModel.save(row, {
            wait: true,
            contentType: "application/json; charset=UTF-8",
      
            success: (model: Model, response: Response) => {
              console.log(response)
            },
            error: (model: Model, response: Response) => {
              console.error("Failed to update card:", response);
              showLogAlert(
                "Error: faltan datos. Por favor, rellene todos los campos.",
                "#f44336"
              );
            },
          });
         
        });
    
        showLogAlert("Tablero exportado con éxito", "#1D4355");

        // Llamar a deleteBoard pasando el contexto
        this.deleteBoard(undefined, undefined, true);
        (document.getElementById("exportDialog") as ojDialog).close();
      })
      .catch((error: Error) => {
        console.error("Error al cargar la colección:", error);
      });
    })
    
    this.boardCol()
    .fetch()
    .then(() => {
      const myModelSend = this.boardCol().get(this.sendBoardId());
      const myModelReceive = this.boardCol().get(this.receivedBoardId());

      this.board().id = this.receivedBoardId()
      this.board().name = myModelReceive.get("name")
      const mergedUsersSet = new Set([...myModelReceive.get("users"), ...myModelSend.get("users")]);
      const mergedUsers: User[] = [...mergedUsersSet];

      this.board().users = mergedUsers;
      this.updateBoard()
      
    })
   
  };

  selectedBoardChanged = async (
    event: ojListView.selectedChanged<Board["id"], Board>
  ) => {
    const selectedBoardId = event.detail.value; // Nos devuelve un KeySet de los id de las tarjetas seleccionada
    const keys = (selectedBoardId as KeySetImpl<Board["id"]>).keys.keys as Set<number>; // Conseguir la clave en conjunto de tipo Set
    const myModel = this.boardCol().get(JSON.stringify(Array.from(keys.values())[0]));

    sessionStorage.setItem("boardId", myModel.get("id"));
    sessionStorage.setItem("boardName", myModel.get("name"));
    if(JSON.stringify(Array.from(keys.values())[0]) != null) window.location.href = "http://localhost:8000/?ojr=boards";
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
  transitionCompleted(): void {
    // implement if needed
  }
}

export = BoardsListViewModel;
