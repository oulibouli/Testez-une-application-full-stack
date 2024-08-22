import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockAuthService: Partial<AuthService>;
  let mockSessionService: Partial<SessionService>;
  let router: Router

  beforeEach(async () => {
    mockAuthService = {
      login: jest.fn().mockReturnValue(of({
        token: '1234',
        type: 'Bearer',
        id: 1,
        username: 'username',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      }))
    }
    mockSessionService = {
      logIn: jest.fn().mockReturnValue({})
    }
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        {provide: SessionService, useValue: mockSessionService},
        {provide: AuthService, useValue: mockAuthService},
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
    .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should login and navigate to sessions', () => {
    jest.spyOn(router, 'navigate')
    const mockLoginRequest: LoginRequest = {
        email: "test@test.com",
        password: "password"
    }
    const mockSessionInformation: SessionInformation = {
      token: '1234',
        type: 'Bearer',
        id: 1,
        username: 'username',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
    }
    component.form.setValue(mockLoginRequest)
    component.submit()

    expect(mockAuthService.login).toHaveBeenCalledWith(mockLoginRequest)
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSessionInformation)
    expect(router.navigate).toHaveBeenCalledWith(['/sessions'])
  })

  it('should set error to true when login fails', () => {
    const loginErrorMock = new Error('login failed');
    (mockAuthService.login as jest.Mock).mockReturnValueOnce(throwError(() => loginErrorMock))

    const mockLoginRequest: LoginRequest = {
      email: "test@test.com",
      password: "password"
    }

    component.form.setValue(mockLoginRequest)
    component.submit()
    expect(component.onError).toBeTruthy
  })
});
