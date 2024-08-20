import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { Router } from '@angular/router';
import { SessionService } from './services/session.service';
import { SessionInformation } from './interfaces/sessionInformation.interface';
import { Observable } from 'rxjs';


describe('AppComponent', () => {
  let router: Router;
  let sessionService: SessionService;
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();

    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should logOut and empty router.navigate', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    // Espionner la méthode navigate du Router
    const navigateSpy = jest.spyOn(router, 'navigate');

    // Espionner la méthode logOut du SessionService
    const logOutSpy = jest.spyOn(sessionService, 'logOut');

    app.logout();

    expect(logOutSpy).toHaveBeenCalled(); // Vérifie si logOut a été appelé
    expect(navigateSpy).toHaveBeenCalledWith(['']); // Vérifie si navigate a été appelé avec la route attendue
  })

  it('should return the observable $isLogged', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    const spy = jest.spyOn(sessionService, '$isLogged')

    const isLoggedObservable = app.$isLogged()

    expect(spy).toHaveBeenCalled()

    expect(isLoggedObservable).toBeInstanceOf(Observable)
  })

  it('should check the state observable when login logout ', (done) => {
   const mockSession: SessionInformation =  {
    token: '1234',
    type: 'Bearer',
    id: 1,
    username: 'username',
    firstName: 'user',
    lastName: 'name',
    admin: false
  };
    sessionService.$isLogged().subscribe(isLogged => {
      if(!isLogged){
        expect(isLogged).toBeFalsy()
        sessionService.logIn(mockSession)
      }
      else {
        expect(isLogged).toBeTruthy()
        done()
      }
    })
  })
});
