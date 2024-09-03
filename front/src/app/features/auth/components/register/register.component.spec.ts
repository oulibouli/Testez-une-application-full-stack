import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { spyOn } from 'jest-mock';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: jest.Mocked<AuthService>;
  let router: jest.Mocked<Router>;

  beforeEach(() => {

    authService = {
      register: jest.fn(),
      login: jest.fn(),
    } as unknown as jest.Mocked<AuthService>;

    router = {
      navigate: jest.fn(),
    } as unknown as jest.Mocked<Router>;

    TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  it('should pass to register the form inputs', () => {
    const mockUser = {
      lastName: 'Admin',
      firstName: 'Admin',
      email: 'yoga@Studio.com',
      password: 'test!1234',
    };

    const authService = (component as any).authService;
    const registerSpy = spyOn(authService, 'register') as jest.SpyInstance;
    registerSpy.mockReturnValue(of({}));

    component.form.controls['firstName'].setValue(mockUser.firstName);
    component.form.controls['lastName'].setValue(mockUser.lastName);
    component.form.controls['email'].setValue(mockUser.email);
    component.form.controls['password'].setValue(mockUser.password);

    component.submit();

    expect(registerSpy).toHaveBeenCalledWith(mockUser);
  });

  it('should disable the submit button if an input is wrong or missing', () => {
    component.form.controls['firstName'].setValue('Admin');
    component.form.controls['lastName'].setValue('Admin');
    component.form.controls['email'].setValue(''); // Empty email
    component.form.controls['password'].setValue('test!1234');

    expect(component.form.valid).toBe(false);
    
  });
});