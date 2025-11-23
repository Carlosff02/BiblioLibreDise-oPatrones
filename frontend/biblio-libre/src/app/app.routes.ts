import { Routes } from '@angular/router';
import { Principal } from './features/pages/principal/principal';
import { BusquedaComponent } from './features/pages/busqueda.component/busqueda.component';
import { VisorLibroComponent } from './features/pages/visor-libro.component/visor-libro.component';
import { VisorEpub } from './features/pages/visor-epub/visor-epub';
import { HomeComponent } from './features/pages/home-page/home.component';

export const routes: Routes = [
  {path:'', component:HomeComponent, children:
    [
      {path:'', component:Principal,

      },
      {path:'busqueda', component:BusquedaComponent},
         {path:'busqueda/visor/:titulo', component:VisorLibroComponent},

    ]
  },
  {path:'busqueda/visor-epub/:titulo', component:VisorEpub}
];
