import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    // Set up the testing module with HttpClientTestingModule
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    // Inject the AuthService and HttpTestingController
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  // Verify that there are no pending HTTP requests after each test
  afterEach(() => {
    httpMock.verify(); // Check that all the requests are executed
  });

  // Verify that the AuthService is created successfully
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test the register method for a new user
  it('should register a new user', () => {
    let registerMock: RegisterRequest = {
      email: 'test@test.com',
      firstName: 'user',
      lastName: 'user',
      password: 'password'
    };

    // Call the register method and expect a void observable response
    service.register(registerMock).subscribe(response => {
      expect(response).toBeUndefined(); // Return a void observable
    });

    // Expect an HTTP POST request to the register endpoint
    const req = httpMock.expectOne(`${service['pathService']}/register`);
    expect(req.request.method).toBe('POST');
    req.flush(null); // Simulate a response with no body (null)
  });

  // Test the login method for an existing user
  it('should login a user', () => {
    let sessionMock: LoginRequest = {
      email: "test@test.com",
      password: 'string'
    };
    
    // Mocked response for a successful login
    const responseMock: SessionInformation = {
      token: 'test-token',
      type: 'Bearer',
      id: 1,
      username: 'test',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };

    // Call the login method and expect the mocked response
    service.login(sessionMock).subscribe(response => {
      expect(response).toEqual(responseMock);
    });

    // Expect an HTTP POST request to the login endpoint
    const req = httpMock.expectOne(`${service['pathService']}/login`);
    expect(req.request.method).toBe('POST');
    req.flush(responseMock); // Simulate a response with the mocked data
  });
});