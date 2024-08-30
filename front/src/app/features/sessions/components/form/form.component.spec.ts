import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let route: ActivatedRoute;
  let mockSessionApiService: Partial<SessionApiService>;
  let matSnackBarMock: Partial<MatSnackBar>;

  // Mock the session service with admin rights
  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  beforeEach(async () => {
    // Mock the services used by the component
    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of({
        id: 426,
        name: 'name',
        description: 'desc',
        date: new Date("2024-08-22T10:32:09.475Z"),
        teacher_id: 1,
        users: [123],
        createdAt: new Date("2024-08-22T10:32:09.475Z"),
        updatedAt: new Date("2024-08-22T10:32:09.475Z")
      })),
      create: jest.fn().mockReturnValue(of({
        id: 426,
        name: 'name',
        description: 'desc',
        date: new Date("2024-08-22T10:32:09.475Z"),
        teacher_id: 1,
        users: [123],
        createdAt: new Date("2024-08-22T10:32:09.475Z"),
        updatedAt: new Date("2024-08-22T10:32:09.475Z")
      })),
      update: jest.fn().mockReturnValue(of({
        id: 426,
        name: 'name',
        description: 'desc',
        date: new Date("2024-08-22T10:32:09.475Z"),
        teacher_id: 1,
        users: [123],
        createdAt: new Date("2024-08-22T10:32:09.475Z"),
        updatedAt: new Date("2024-08-22T10:32:09.475Z")
      })),
    };
    matSnackBarMock = {
      open: jest.fn()
    };

    // Configure the testing module
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: ActivatedRoute,
          useValue: { 
            snapshot: { 
              paramMap: {
                get: jest.fn() 
              }
            } 
          }
        }
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    // Initialize component, router, and activated route
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    route = TestBed.inject(ActivatedRoute);
  });

  // Verify that the component is created successfully
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test navigation to /sessions if the user is not an admin
  it('should navigate to /sessions if not an admin', () => {
    mockSessionService.sessionInformation.admin = false;
    jest.spyOn(router, 'navigate');

    component.ngOnInit();
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  // Test fetching session details and initializing the form if URL contains "update"
  it('should fetch session details and initialize form if URL contains "update"', () => {
    const mockId = '426';

    mockSessionService.sessionInformation.admin = true;
    jest.spyOn(router, 'url', 'get').mockReturnValue(`/update/${mockId}`);
    jest.spyOn(route.snapshot.paramMap, 'get').mockReturnValue(mockId);
    const detailSpy = jest.spyOn(mockSessionApiService, 'detail');

    component.ngOnInit();
    expect(detailSpy).toHaveBeenCalledWith(mockId);
  });

  // Test creating a session when not updating an existing one
  it('should create a session if not an update', () => {
    component.onUpdate = false;
    jest.spyOn(router, 'navigate');
    const mockSession = {
      name: 'name',
      description: 'desc',
      date: new Date("2024-08-22T10:32:09.475Z"),
      teacher_id: 1
    };

    component.sessionForm?.setValue(mockSession);

    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalledWith(mockSession);
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });
  
  // Test updating an existing session
  it('should update a session if an update', () => {
    component.onUpdate = true;
    jest.spyOn(router, 'navigate');
    (component as any).id = '1';
    const mockSession = {
      name: 'name',
      description: 'desc',
      date: new Date("2024-08-22T10:32:09.475Z"),
      teacher_id: 1
    };
    const mockId = '1';

    component.sessionForm?.setValue(mockSession);

    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith(mockId, mockSession);
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });
});