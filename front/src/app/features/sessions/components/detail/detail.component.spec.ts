import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { TeacherService } from 'src/app/services/teacher.service';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let mockSessionService: Partial<SessionService>;
  let mockSessionApiService: Partial<SessionApiService>;
  let mockTeacherService: Partial<TeacherService>;
  let spyHistoryBack: jest.SpyInstance;
  let matSnackBarMock: Partial<MatSnackBar>;
  let router: Router

  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: {
        token: '1234',
        type: 'Bearer',
        id: 1,
        username: 'username',
        firstName: 'user',
        lastName: 'name',
        admin: false
      }
    };
    
    matSnackBarMock = {
      open: jest.fn()
    };
    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of({
        id: 1,
        name: 'Test Session',
        users: [123],
        teacher_id: 456,
        description: 'desc',
        date: new Date("2024-08-22T10:32:09.475Z")
      })),
      delete: jest.fn().mockReturnValue(of({})),
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({}))
    };

    mockTeacherService = {
      detail: jest.fn().mockReturnValue(of({
        id: 1,
        lastName: 'lastName',
        firstName: 'firstName',
        createdAt: new Date("2024-08-22T10:32:09.475Z"),
        updatedAt: new Date("2024-08-22T10:32:09.475Z")
      }))
    }

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatButtonModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: TeacherService, useValue: mockTeacherService },
      ],
    })
    .compileComponents();

    spyHistoryBack = jest.spyOn(window.history, 'back');
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
  });

  afterEach(() => {
    spyHistoryBack.mockRestore();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should go back to the previous page from history', () => {
    component.back();
    expect(spyHistoryBack).toHaveBeenCalled();
  });

  it('should call sessionApiService.delete and navigate to sessions after deletion', () => {
    jest.spyOn(router, 'navigate')
    component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalledWith(component.sessionId);
    expect(matSnackBarMock.open).toHaveBeenCalledWith(
      'Session deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(router.navigate).toHaveBeenCalledWith(['sessions'])
  });
  it('should call sessionApiService.participate and refresh session details', () => {
     // Fake values
    component.sessionId = 'mock-session-id';
    component.userId = 'mock-user-id';

    const mockTeacher: Teacher = {
        id: 1,
        lastName: 'lastName',
        firstName: 'firstName',
        createdAt: new Date("2024-08-22T10:32:09.475Z"),
        updatedAt: new Date("2024-08-22T10:32:09.475Z")
      }

    const mockSession: Session = {
        id: 1,
        name: 'Test Session',
        users: [123],
        teacher_id: 456,
        description: 'desc',
        date: new Date("2024-08-22T10:32:09.475Z")
    }

    const participateSpy = jest.spyOn(mockSessionApiService, 'participate').mockReturnValue(of())
    
    component.participate()
    expect(participateSpy).toHaveBeenCalledWith(component.sessionId, component.userId)

    fixture.detectChanges()
    expect(component.session).toEqual(mockSession)
    expect(component.teacher).toEqual(mockTeacher)
  })
  
  it('should call sessionApiService.unparticipate and refresh session details', () => {
    component.sessionId = 'mock-session-id';
    component.userId = 'mock-user-id';

    const mockTeacher: Teacher = {
        id: 1,
        lastName: 'lastName',
        firstName: 'firstName',
        createdAt: new Date("2024-08-22T10:32:09.475Z"),
        updatedAt: new Date("2024-08-22T10:32:09.475Z")
      }

    const mockSession: Session = {
        id: 1,
        name: 'Test Session',
        users: [123],
        teacher_id: 456,
        description: 'desc',
        date: new Date("2024-08-22T10:32:09.475Z")
    }

    const unParticipateSpy = jest.spyOn(mockSessionApiService, 'unParticipate').mockReturnValue(of())
    
    component.unParticipate()
    expect(unParticipateSpy).toHaveBeenCalledWith(component.sessionId, component.userId)

    fixture.detectChanges()
    expect(component.session).toEqual(mockSession)
    expect(component.teacher).toEqual(mockTeacher)

  })
});
