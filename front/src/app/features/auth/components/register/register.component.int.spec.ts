import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let mockAuthService: Partial<AuthService>;
  let router: Router;

  beforeEach(async () => {
    // Mock the AuthService with a successful registration response
    mockAuthService = {
      register: jest.fn().mockReturnValue(of(null))
    };
    
    // Configure the testing module
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        {provide: AuthService, useValue: mockAuthService}
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    // Create the component instance and trigger change detection
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
  });
  

  // Test the registration process and navigation to the login page
  it('should register user and navigate to /login', () => {
    // Define a mock registration request
    const mockRegisterRequest: RegisterRequest = {
        email: 'test@test.com',
        firstName: 'firstname',
        lastName: 'lastname',
        password: 'password'
    };
    
    // Set form values and submit the form
    component.form.setValue(mockRegisterRequest);
    jest.spyOn(router, 'navigate');
    component.submit();
    
    // Verify the interactions and navigation
    expect(mockAuthService.register).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  // Test handling of registration failure with an error
  it('should send an error when register fails', () => {
    // Simulate a registration failure
    const mockError = new Error('register failed');
    (mockAuthService.register as jest.Mock).mockReturnValueOnce(throwError(() => mockError));

    // Define a mock registration request
    const mockRegisterRequest: RegisterRequest = {
      email: 'test@test.com',
      firstName: 'firstname',
      lastName: 'lastname',
      password: 'password'
    };
    
    // Set form values and submit the form
    component.form.setValue(mockRegisterRequest);
    component.submit();

    // Verify that the error flag is set to true
    expect(component.onError).toBeTruthy();
  });
});