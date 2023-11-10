import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable, catchError, throwError } from "rxjs";
import { ListParams } from "src/app/interfaces/util/ListParams";
import { PageModel } from "src/app/interfaces/util/PageModel";

export class EntityService<T> {

    constructor(
        protected http: HttpClient,
        protected entityUrl: string
    ) {
        this.http = http;
        this.entityUrl = entityUrl;
    }

    public getListQueryParams(listParams?: ListParams) {
        if (listParams) {
            let { page = 0, size = 10, sort = [], filtro = '', order } = listParams;
    
            let params = new HttpParams();
        
            if (sort && sort.length) {
                params = params.append(
                'orderby',
                sort
                    .map(s => {
                    let order = '';
                    if (s.order === 1) { order = ' asc'; }
                    else if (s.order === -1) { order = ' desc'; }
                    return `${s.field}${order}`;
                    })
                    .join(', ')
                );
            }
        
            if (filtro) { params = params.append('filter', filtro); }
        
            params = params.append('size', String(size));
            params = params.append('page', String(page));
            if (order) {
                params = params.append('order', String(order));
            }
        
            return params;
        } else {
            const params = {} as any;
            return params;
        }
    }

    public defaultCatch() {
        return catchError((err: any) => {
          if (err) {
            this.doError(err);
          }
    
          return throwError(err);
        });
      }
    
    public doError(err: any) {
        const genericError =
          'Erro no servidor, tente novamente em alguns minutos.';
        let detail = genericError;
        if (err.error && err.error.errors) {
          detail = err.error.errors.map((e: any) => e.message).join(', ');
        } else if (err.error && err.error.msg) {
          detail = err.error && err.error.msg;
        }
      }

      public create(entity: T) {
        return this.http
          .post<T>(`${this.entityUrl}`, entity)
          .pipe(this.defaultCatch());
      }
      
      public retrieveAll(listParams?: ListParams): Observable<PageModel<T>> {
        return this.http
          .get<PageModel<T>>(this.entityUrl, {
            params: this.getListQueryParams(listParams)
          })
          .pipe(this.defaultCatch()) as any;
      }
    
      public retrieve(id: string){
        return this.http
          .get<T>(`${this.entityUrl}/${id}`)
          .pipe(this.defaultCatch());
      }
    
    
      public update(id: string, entity: T) {
        return this.http
          .put<T>(`${this.entityUrl}/${id}`, entity)
          .pipe(this.defaultCatch());
      }
    
      public delete(id: string) {
        return this.http
          .delete<T>(`${this.entityUrl}/${id}`)
          .pipe(this.defaultCatch());
      }
}