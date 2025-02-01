import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Geschaeftspartner } from "../../models/geschaeftspartner";
import { environment } from "../../../environments/environment";

/*
  This service is used to communicate with the backend API for the Geschaeftspartner entity.
  It provides methods to get, add, update and delete Geschaeftspartner.
  The backend URL is defined in the environment file. The HttpClient is used to send requests to the backend and must be provided in app.config.ts.
*/

@Injectable({
  providedIn: 'root',
})
export class GeschaeftspartnerAPIService {
  private readonly BASE_URL = environment.backendUrl;
  private http = inject(HttpClient);

  getGeschaeftspartner() : any {
    return this.http.get(this.BASE_URL + 'geschaeftspartner');
  }

  getGeschaeftspartnerById(id: number) : any {
    return this.http.get(this.BASE_URL + 'geschaeftspartner/' + id);
  }

  addGeschaeftspartner(geschaeftspartner: Geschaeftspartner) : any {
    return this.http.post(this.BASE_URL + 'geschaeftspartner', geschaeftspartner);
  }

  updateGeschaeftspartner(geschaeftspartner: Geschaeftspartner) : any {
    return this.http.put(this.BASE_URL + 'geschaeftspartner/' + geschaeftspartner.id, geschaeftspartner);
  }

  deleteGeschaeftspartner(id: number) : any {
    return this.http.delete(this.BASE_URL + 'geschaeftspartner/' + id);
  }
}