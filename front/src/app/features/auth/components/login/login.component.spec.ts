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
    // Mock the AuthService with a successful login response
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
    };
    
    // Mock the SessionService for handling session information
    mockSessionService = {
      logIn: jest.fn().mockReturnValue({})
    };
    
    // Configure the testing module
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
        ReactiveFormsModule
      ]
    })
    .compileComponents();
    
    // Create the component instance and trigger change detection
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
  });

  // Verify that the component is created successfully
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test the login and navigation to the sessions page
  it('should login and navigate to sessions', () => {
    jest.spyOn(router, 'navigate');
    
    // Define a mock login request and session information
    const mockLoginRequest: LoginRequest = {
        email: "test@test.com",
        password: "password"
    };
    const mockSessionInformation: SessionInformation = {
      token: '1234',
      type: 'Bearer',
      id: 1,
      username: 'username',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: false
    };
    
    // Set form values and submit the form
    component.form.setValue(mockLoginRequest);
    component.submit();

    // Verify the interactions and navigation
    expect(mockAuthService.login).toHaveBeenCalledWith(mockLoginRequest);
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSessionInformation);
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  // Test handling of login failure with an error
  it('should set error to true when login fails', () => {
    // Simulate a login failure
    const loginErrorMock = new Error('Invalid credentials');
    (mockAuthService.login as jest.Mock).mockReturnValueOnce(throwError(() => loginErrorMock));

    // Define a mock login request
    const mockLoginRequest: LoginRequest = {
      email: "test@test.com",
      password: "password"
    };

    // Set form values and submit the form
    component.form.setValue(mockLoginRequest);
    component.submit();

    // Verify that the error flag is set to true
    expect(component.onError).toBeTruthy();
  });
});