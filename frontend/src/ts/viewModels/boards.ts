/**
 * @license
 * Copyright (c) 2014, 2024, Oracle and/or its affiliates.
 * Licensed under The Universal Permissive License (UPL), Version 1.0
 * as shown at https://oss.oracle.com/licenses/upl/
 * @ignore
 */
import "demo-component/loader";
import "ojs/ojavatar";
import { ojButtonEventMap } from "ojs/ojbutton";
import { IntlConverterUtils } from "ojs/ojconverterutils-i18n";
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

type Board = {
  id: number;
  name: string;
};

type Status = {
  id: number;
  name: string;
};

type Role = {
  id: number;
  name: string;
};

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

class BoardsViewModel {
  // Conseguir el userID del usuario iniciado la sessión
  userID: string;
  boardID: number;
  boardName: string;

  // Icono del perfil de los usuarios
  initials: string | undefined;

  // URL del servidor del Card
  ServerURLCards = "http://localhost:8080/cards";

  // Observables para los User
  user: ko.Observable<User>;

  // Observables para los Card
  card: ko.Observable<Card>;
  cardCol: ko.Observable = ko.observable();
  cardSource: ko.Observable = ko.observable();
  // Observable para cada estado de tarjetas
  cardColTodo: ko.Observable = ko.observable();
  // cardSourceTodo: ko.Observable = ko.observable();
  cardSourceTodo: ko.Observable = ko.observable(
    new CollectionDataProvider(new Collection())
  );
  cardColDoing: ko.Observable = ko.observable();
  // cardSourceDoing: ko.Observable = ko.observable();
  cardSourceDoing: ko.Observable = ko.observable(
    new CollectionDataProvider(new Collection())
  );
  cardColBlocked: ko.Observable = ko.observable();
  // cardSourceBlocked: ko.Observable = ko.observable();
  cardSourceBlocked: ko.Observable = ko.observable(
    new CollectionDataProvider(new Collection())
  );
  cardColDone: ko.Observable = ko.observable();
  // cardSourceDone: ko.Observable = ko.observable();
  cardSourceDone: ko.Observable = ko.observable(
    new CollectionDataProvider(new Collection())
  );

  // Observables for Activity Items
  userSelected = ko.observable(false);
  cardSelected: ko.Observable;
  selectedItemTodo: ObservableKeySet<Card>;
  selectedItemDoing: ObservableKeySet<Card>;
  selectedItemBlocked: ObservableKeySet<Card>;
  selectedItemDone: ObservableKeySet<Card>;

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

  private dragItemId;

  // Observables para los User
  private userCol: ko.Observable = ko.observable();
  userDatasourse: ko.Observable = ko.observable();

  constructor() {
    const ssUser = sessionStorage.getItem("user");
    const ssBoard = sessionStorage.getItem("boardId");
    const nameBoard = sessionStorage.getItem("boardName");

    const jsonObject = JSON.parse(ssUser as string);
    this.userID = jsonObject.userId;
    this.boardID = ssBoard as unknown as number;
    this.boardName = nameBoard as string;

    console.log(this.boardID);

    this.user = ko.observable({
      userId: jsonObject.userId,
      email: jsonObject.email,
      firstName: jsonObject.firstName,
      lastName: jsonObject.lastName,
      password: jsonObject.password,
      role: {
        id: jsonObject.roleId,
        name: jsonObject.roleName,
      },
      boards: jsonObject.boards,
    });

    this.card = ko.observable<Card>({
      id: 0,
      title: "",
      description: "",
      startDate: "",
      endDate: "",
      priority: 1,
      board: {
        id: 1,
        name: "prueba",
      },
      status: {
        id: 1,
        name: "todo",
      },
      user: {
        ...this.user(),
      },
    });

    const cardColAux = this.createCollectionCards();
    this.cardCol(cardColAux);
    this.cardSource(new CollectionDataProvider(this.cardCol()));

    this.cardColTodo(this.createCollectionCards());
    this.cardSourceTodo(new CollectionDataProvider(this.cardColTodo()));
    this.cardColDoing(this.createCollectionCards());
    this.cardSourceDoing(new CollectionDataProvider(this.cardColDoing()));
    this.cardColBlocked(this.createCollectionCards());
    this.cardSourceBlocked(new CollectionDataProvider(this.cardColBlocked()));
    this.cardColDone(this.createCollectionCards());
    this.cardSourceDone(new CollectionDataProvider(this.cardColDone()));

    this.cardFilter();

    this.cardSelected = ko.observable();
    this.selectedItemTodo = new ObservableKeySet(new KeySetImpl());
    this.selectedItemDoing = new ObservableKeySet(new KeySetImpl());
    this.selectedItemBlocked = new ObservableKeySet(new KeySetImpl());
    this.selectedItemDone = new ObservableKeySet(new KeySetImpl());

    this.dragItemId = 0;

    // conseguir los dos letras para el icono del perfil
    this.initials = IntlConverterUtils.getInitials(
      this.user().firstName,
      this.user().lastName
    );

    this.userCol(this.createCollectionUser());
    this.userCol()
      .fetch()
      .then(() => {
        this.getUser();
      })
      .catch((error: Error) => {
        console.error("Error al cargar la colección:", error);
      });

    this.userDatasourse(new CollectionDataProvider(this.userCol()));
  }

  handleStart = (event: DragEvent) => {
    if (event.dataTransfer !== null) {
      const dataStr = event.dataTransfer.getData(
        "application/ojlistviewitems+json"
      );
      const data = JSON.parse(dataStr);
      this.dragItemId = data[0].id;
    }
  };

  handleEnd = (event: DragEvent) => {
    if (
      event.dataTransfer !== null &&
      event.dataTransfer.dropEffect !== "none"
    ) {
      this._removeSourceItem();
    }
  };

  handleDrop = (
    listId: string,
    event: DragEvent,
    context: ojListView.ItemsDropContext
  ) => {
    event.preventDefault();

    let index = -1;
    if (context.item) {
      const itemContext = (
        document.getElementById(listId) as ojListView<Board["id"], Board>
      ).getContextByNode(context.item);
      if (itemContext !== null) {
        index = itemContext.index;
        if (context.position === "after") {
          index += 1;
        }
      }
    }
    this._handleDataTransfer(event.dataTransfer, index, listId);
  };

  private _handleDataTransfer = (
    dataTransfer: any,
    index: number,
    listId: string
  ) => {
    const dataStr = dataTransfer.getData("application/ojlistviewitems+json");

    const data = JSON.parse(dataStr)[0];

    this._insertTargetItem(data, index, listId);
  };

  private _insertTargetItem = (data: any, index: number, listId: string) => {
    const arr = this.cardCol();

    if (listId === "activitiesList1") {
      data.status.id = 1;
      data.status.name = "todo";
    } else if (listId === "activitiesList2") {
      data.status.id = 2;
      data.status.name = "doing";
    } else if (listId === "activitiesList3") {
      data.status.id = 3;
      data.status.name = "blocked";
    } else if (listId === "activitiesList4") {
      data.status.id = 4;
      data.status.name = "done";
    }

    this.card(data);

    this.updateCard();
  };

  private _removeSourceItem = () => {
    this.cardCol.valueHasMutated();
  };

  // Inicializar el observable de la tarjeta
  public inicializar = () => {
    this.card({
      id: 0,
      title: "",
      description: "",
      startDate: "",
      endDate: "",
      priority: 1,
      board: {
        id: 1,
        name: "prueba",
      },
      status: {
        id: 1,
        name: "todo",
      },
      user: { ...this.user() },
    });
  };

  public cardFilter = () => {
    this.cardCol().fetch({
      success: (collection: Collection) => {
        const createFilteredCollection = (filteredData: Card[]) => {
          const modelArray = filteredData.map(
            (card) => new (this.cardCol().model)(card)
          );
          return new (Collection.extend({
            model: this.cardCol().model,
            url: this.ServerURLCards,
          }))(modelArray);
        };

        const data = collection.toJSON() as Card[];

        const board = data.filter((card) => card.board.id == this.boardID);

        this.cardCol(createFilteredCollection(board));

        this.cardSource(new CollectionDataProvider(this.cardCol()));

        // Filtrado de información
        const todo = board.filter((card) => card.status.name === "todo");
        const doing = board.filter((card) => card.status.name === "doing");
        const blocked = board.filter((card) => card.status.name === "blocked");
        const done = board.filter((card) => card.status.name === "done");

        // Crear colecciones filtradas

        // Actualiza la colección inicial
        // this.cardCol().reset(board);
        console.log(this.cardCol());

        // Asignar las colecciones filtradas a los observables
        this.cardColTodo(createFilteredCollection(todo));
        this.cardColDoing(createFilteredCollection(doing));
        this.cardColBlocked(createFilteredCollection(blocked));
        this.cardColDone(createFilteredCollection(done));

        // Actualiza los Source con las colecciones ya filtradas
        this.cardSourceTodo(new CollectionDataProvider(this.cardColTodo()));
        this.cardSourceDoing(new CollectionDataProvider(this.cardColDoing()));
        this.cardSourceBlocked(
          new CollectionDataProvider(this.cardColBlocked())
        );
        this.cardSourceDone(new CollectionDataProvider(this.cardColDone()));
      },
      error: (collection: Collection, error: any) => {
        console.error("Error fetching data:", error);
      },
    });
  };

  // private updateFilteredCollections = () => {
  //   console.log(this.cardCol())
  //   this.cardCol().fetch({

  //     success: (collection: Collection) => {
  //       const createFilteredCollection = (filteredData: Card[]) => {
  //         const modelArray = filteredData.map(
  //           (card) => new (this.cardCol().model)(card)
  //         );
  //         return new (Collection.extend({ model: this.cardCol().model }))(
  //           modelArray
  //         );
  //       };

  //       const data = this.cardCol().toJSON() as Card[];

  //       const todo = data.filter((card) => card.status.name === "todo");
  //       const doing = data.filter((card) => card.status.name === "doing");
  //       const blocked = data.filter((card) => card.status.name === "blocked");
  //       const done = data.filter((card) => card.status.name === "done");

  //       this.cardColTodo(createFilteredCollection(todo));
  //       this.cardColDoing(createFilteredCollection(doing));
  //       this.cardColBlocked(createFilteredCollection(blocked));
  //       this.cardColDone(createFilteredCollection(done));

  //       this.cardSourceTodo(new CollectionDataProvider(this.cardColTodo()));
  //       this.cardSourceDoing(new CollectionDataProvider(this.cardColDoing()));
  //       this.cardSourceBlocked(
  //         new CollectionDataProvider(this.cardColBlocked())
  //       );
  //       this.cardSourceDone(new CollectionDataProvider(this.cardColDone()));
  //     },
  //     error: (collection: Collection, error: any) => {
  //       console.error("Error fetching data:", error);
  //     },
  //   });
  // };

  isCardSourceEmpty = (statusID: number) => {
    switch (statusID) {
      case 1:
        return this.cardSourceTodo().isEmpty();
      case 2:
        return this.cardSourceDoing().isEmpty();
      case 3:
        return this.cardSourceBlocked().isEmpty();
      case 4:
        return this.cardSourceDone().isEmpty();
    }
  };

  // Crear la colección de las tarjetas
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

  // Crear la colección de los usuarios
  public createCollectionUser = () => {
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
    let model = this.userCol().get(this.userID);

    let boardsData = model.get("boards");

    // Mapear los tableros si existen
    let boards = boardsData
      ? boardsData.board.map((board: any) => ({
          id: board.id,
          name: board.name,
        }))
      : [];

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
        board: boards,
      },
    };

    this.user(userAux);
  };

  public computeInitials = (fn: string, ln: string) => {
    return IntlConverterUtils.getInitials(fn, ln);
  };

  // Mostrar el dialogo del crear tarjeta
  public showCreateDialog = (event: ojButtonEventMap["ojAction"]) => {
    if (this.card().title != "") this.inicializar();
    (document.getElementById("createDialog") as ojDialog).open();
  };

  // Mostrar el dialogo del editar junto eliminar tarjeta
  public showEditDialog = (event: ojButtonEventMap["ojAction"]) => {
    this.card(this.cardSelected());
    (document.getElementById("editDialog") as ojDialog).open();
  };

  // Crear Tarjeta
  createCard = async () => {
    const row = {
      cardId: this.card().id,
      title: this.card().title,
      description: this.card().description,
      startDate: this.card().startDate,
      endDate: this.card().endDate,
      priority: this.card().priority,
      boardId: this.boardID,
      statusId: this.card().status.id,
      userId: this.card().user.userId,
    };

    console.log("Datos que se van a enviar: ", JSON.stringify(row));

    this.cardCol().create(row, {
      method: "POST",
      wait: true,
      contentType: "application/json; charset=UTF-8",

      success: (model: Model, response: Response) => {
        console.log("Card created successfully:", response);
        this.cardFilter();
        showLogAlert("Tarjeta creada con éxito", "#1D4355");
        (document.getElementById("createDialog") as ojDialog).close();
      },
      error: (model: Model, response: Response) => {
        console.error("Failed to create card:", response);
        showLogAlert(
          "Error: faltan datos. Por favor, rellene todos los campos.",
          "#f44336"
        );
      },
    });
  };

  // Eliminar Tarjeta
  deleteCard = async () => {
    const myModel = this.cardCol().get(this.card().id);
    if (myModel) {
      this.cardCol().remove(myModel);
      myModel.destroy().then(() => {
        this.cardFilter();
        showLogAlert("Tarjeta eliminada con éxito", "#1D4355");
        (document.getElementById("editDialog") as ojDialog).close();
      });
    }
  };

  // Actualizar Tarjeta
  updateCard = async () => {
    const myModel = this.cardCol().get(this.card().id);
    console.log(this.cardCol());

    const row = {
      cardId: this.card().id,
      title: this.card().title,
      description: this.card().description,
      startDate: this.card().startDate,
      endDate: this.card().endDate,
      priority: this.card().priority,
      boardId: this.boardID,
      statusId: this.card().status.id,
      userId: this.card().user.userId,
    };

    myModel.save(row, {
      wait: true,
      contentType: "application/json; charset=UTF-8",

      success: (model: Model, response: Response) => {
        console.log("Card updated successfully:", response);
        this.cardFilter();
        showLogAlert("Tarjeta actualizada con éxito", "#1D4355");
        (document.getElementById("editDialog") as ojDialog).close();
      },
      error: (model: Model, response: Response) => {
        console.error("Failed to update card:", response);
        showLogAlert(
          "Error: faltan datos. Por favor, rellene todos los campos.",
          "#f44336"
        );
      },
    });
  };

  selectedItemChangedTodo = async (
    event: ojListView.selectedChanged<Card["id"], Card>
  ) => {
    if (event.detail.updatedFrom === "internal") {
      // Para conseguir la tarjeta seleccionada
      const selectedCardId = event.detail.value; // Nos devuelve un KeySet de los id de las tarjetas seleccionada
      const keys = (selectedCardId as KeySetImpl<Card["id"]>).keys
        .keys as Set<number>; // Conseguir la clave en conjunto de tipo Set
      console.log(keys);
      const selectedCard = await this.cardSourceTodo().fetchByKeys({ keys }); // Extraer del Provider las tarjetas con el id de las claves
      console.log(selectedCard.results.get(Array.from(keys.values())[0]).data); // Obtener los datos
      // Almacenarlo en el observable
      this.cardSelected({
        ...selectedCard.results.get(Array.from(keys.values())[0]).data,
      });

      // if (this.selectedItemDoing().keys) {
      this.selectedItemDoing.clear();
      // }
      // if (this.selectedItemBlocked().keys) {
      this.selectedItemBlocked.clear();
      // }
      // if (this.selectedItemDone().keys) {
      this.selectedItemDone.clear();
      // }

      if (this.user().userId === this.cardSelected().user.id)
        this.userSelected(true);
      else this.userSelected(false);
    }
  };

  selectedItemChangedDoing = async (
    event: ojListView.selectedChanged<Card["id"], Card>
  ) => {
    if (event.detail.updatedFrom === "internal") {
      console.log("doing");

      // Para conseguir la tarjeta seleccionada
      const selectedCardId = event.detail.value; // Nos devuelve un KeySet de los id de las tarjetas seleccionada
      const keys = (selectedCardId as KeySetImpl<Card["id"]>).keys
        .keys as Set<number>; // Conseguir la clave en conjunto de tipo Set
      console.log(keys);
      const selectedCard = await this.cardSourceDoing().fetchByKeys({ keys }); // Extraer del Provider las tarjetas con el id de las claves
      console.log(selectedCard.results.get(Array.from(keys.values())[0]).data); // Obtener los datos
      // Almacenarlo en el observable
      this.cardSelected({
        ...selectedCard.results.get(Array.from(keys.values())[0]).data,
      });

      // if (this.selectedItemTodo().keys) {
      this.selectedItemTodo.clear();
      // }
      // if (this.selectedItemBlocked().keys) {
      this.selectedItemBlocked.clear();
      // }
      // if (this.selectedItemDone().keys) {
      this.selectedItemDone.clear();
      // }
      if (this.user().userId === this.cardSelected().user.id)
        this.userSelected(true);
      else this.userSelected(false);
    }
  };

  selectedItemChangedBlocked = async (
    event: ojListView.selectedChanged<Card["id"], Card>
  ) => {
    if (event.detail.updatedFrom === "internal") {
      console.log("blocked");

      // Para conseguir la tarjeta seleccionada
      const selectedCardId = event.detail.value; // Nos devuelve un KeySet de los id de las tarjetas seleccionada
      const keys = (selectedCardId as KeySetImpl<Card["id"]>).keys
        .keys as Set<number>; // Conseguir la clave en conjunto de tipo Set
      console.log(keys);
      const selectedCard = await this.cardSourceBlocked().fetchByKeys({ keys }); // Extraer del Provider las tarjetas con el id de las claves
      console.log(selectedCard.results.get(Array.from(keys.values())[0]).data); // Obtener los datos
      // Almacenarlo en el observable
      this.cardSelected({
        ...selectedCard.results.get(Array.from(keys.values())[0]).data,
      });

      // if (this.selectedItemTodo().keys) {
      this.selectedItemTodo.clear();
      // }
      // if (this.selectedItemDoing().keys) {
      this.selectedItemDoing.clear();
      // }
      // if (this.selectedItemDone().keys) {
      this.selectedItemDone.clear();
      // }
      if (this.user().userId === this.cardSelected().user.id)
        this.userSelected(true);
      else this.userSelected(false);
    }
  };

  selectedItemChangedDone = async (
    event: ojListView.selectedChanged<Card["id"], Card>
  ) => {
    if (event.detail.updatedFrom === "internal") {
      console.log("done");

      // Para conseguir la tarjeta seleccionada
      const selectedCardId = event.detail.value; // Nos devuelve un KeySet de los id de las tarjetas seleccionada
      const keys = (selectedCardId as KeySetImpl<Card["id"]>).keys
        .keys as Set<number>; // Conseguir la clave en conjunto de tipo Set
      const selectedCard = await this.cardSourceDone().fetchByKeys({ keys }); // Extraer del Provider las tarjetas con el id de las claves
      // Almacenarlo en el observable
      this.cardSelected({
        ...selectedCard.results.get(Array.from(keys.values())[0]).data,
      });

      // if (this.selectedItemTodo().keys) {
      this.selectedItemTodo.clear();
      // }
      // if (this.selectedItemDoing().keys) {
      this.selectedItemDoing.clear();
      // }
      // if (this.selectedItemBlocked().keys) {
      this.selectedItemBlocked.clear();
      // }
      if (this.user().userId === this.cardSelected().user.id)
        this.userSelected(true);
      else this.userSelected(false);
    }
  };

  isUserInBoard = (boardList: Board[]) => {
    let res: boolean = false;
    boardList.forEach((board) => {
      if (board.id == this.boardID) {
        res = true;
      }
    });

    return res;
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

export = BoardsViewModel;
