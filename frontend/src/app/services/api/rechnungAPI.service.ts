import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Rechnung } from "../../models/rechnung";
import { environment } from "../../../environments/environment";

/*
  This service is used to communicate with the backend API for the Rechnung entity.
  It provides methods to get, add, update and delete Rechnung.
  The backend URL is defined in the environment file. The HttpClient is used to send requests to the backend and must be provided in app.config.ts.
*/

@Injectable({
  providedIn: 'root',
})
export class RechnungAPIService {
   private readonly BASE_URL = environment.backendUrl;
private http = inject(HttpClient);

  getRechnung() : any {
    return this.http.get(this.BASE_URL + 'rechnung');
  }

  addRechnung(rechnung: Rechnung) : any {
    return this.http.post(this.BASE_URL + 'rechnung', rechnung);
  }

  updateRechnung(rechnung: Rechnung) : any {
    return this.http.put(this.BASE_URL + 'rechnung/' + rechnung.id, rechnung);
  }

  deleteRechnung(id: number) : any {
    return this.http.delete(this.BASE_URL + 'rechnung/' + id);
  }
}