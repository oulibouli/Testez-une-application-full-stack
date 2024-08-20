import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { of } from 'rxjs';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { User } from 'src/app/interfaces/user.interface';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  let mockSessionService: jest.Mocked<SessionService>
  let mockUserService: jest.Mocked<UserService>
  let router: jest.Mocked<Router>
  const mockUser: User = {
    id: 1,
    email: 'string',
    lastName: 'string',
    firstName: 'string',
    admin: true,
    password: "string",
    createdAt: new Date(),
    updatedAt: new Date()
  };
  
  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: { admin: true, id: 1 },
      logOut: jest.fn()
    } as any;
    mockUserService = {
      getById: jest.fn().mockReturnValue(of(mockUser)),
      delete: jest.fn().mockReturnValue(of(null))
    } as any; 

    // Création d'un mock pour Router
    router = {
      navigate: jest.fn()
    } as any;
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        {provide: SessionService, useValue: mockSessionService},
        {provide: UserService, useValue: mockUserService},
        {provide: Router, useValue: router}
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get userById and subscribe to user observable', () => {
    component.ngOnInit()
    expect(mockUserService).toHaveBeenCalledWith('1')
    expect(component.user).toEqual(mockUser)
  })
});
