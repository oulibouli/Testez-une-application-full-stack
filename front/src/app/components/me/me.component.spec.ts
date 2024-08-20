import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
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
  let matSnackBarMock: jest.Mocked<MatSnackBar>
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
  let spyHistoryBack: jest.SpyInstance
  
  beforeEach(async () => {
    matSnackBarMock = {
      open: jest.fn()
    } as any
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
        {provide: MatSnackBar, useValue: matSnackBarMock},
        {provide: Router, useValue: router}
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    spyHistoryBack = jest.spyOn(window.history, 'back');
  });

  // Cleanup after each test
  afterEach(() => {
    spyHistoryBack.mockRestore(); 
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get userById and subscribe to user observable', () => {
    component.ngOnInit()
    expect(mockUserService.getById).toHaveBeenCalledWith('1')
    expect(component.user).toEqual(mockUser)
  })

  it('should go back to the previous page', () => {
    component.back()
    expect(spyHistoryBack).toHaveBeenCalled()
  })

  it('should delete the user account', () => {
    component.delete()
    expect(mockUserService.delete).toHaveBeenCalledWith('1')
    expect(matSnackBarMock.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    )
  })
});
