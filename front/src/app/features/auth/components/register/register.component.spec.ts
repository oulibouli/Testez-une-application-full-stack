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
  let router: Router

  beforeEach(async () => {
    mockAuthService = {
      register: jest.fn().mockReturnValue(of(null))
    }
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

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should register user and navigate to /login', () => {
    const mockRegisterRequest: RegisterRequest = {
        email: 'test@test.com',
        firstName: 'firstname',
        lastName: 'lastname',
        password: 'password'
    }
    component.form.setValue(mockRegisterRequest)
    jest.spyOn(router, 'navigate')
    component.submit()
    expect(mockAuthService.register).toHaveBeenCalled()
    expect(router.navigate).toHaveBeenCalledWith(['/login'])
  })

  it('should send an error when register fails', () => {
    const mockError = new Error('register failed');
    (mockAuthService.register as jest.Mock).mockReturnValueOnce(throwError(() => mockError))

    const mockRegisterRequest: RegisterRequest = {
      email: 'test@test.com',
      firstName: 'firstname',
      lastName: 'lastname',
      password: 'password'
    }
    component.form.setValue(mockRegisterRequest)

    component.submit()

    expect(component.onError).toBeTruthy()

  })
});
