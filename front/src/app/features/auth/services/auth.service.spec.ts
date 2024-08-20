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
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController)
  });

  afterEach(() => {
    httpMock.verify() // Check that all the requests are executed
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })

  it('should register a new user', () => {
    let registerMock : RegisterRequest = {
        email: 'test@test.com',
        firstName: 'user',
        lastName: 'user',
        password: 'password'
    }
    service.register(registerMock).subscribe(response => {
      expect(response).toBeUndefined() // Return a void observable

    })
      const req = httpMock.expectOne(`${service['pathService']}/register`)
      expect(req.request.method).toBe('POST')
      req.flush(null)
  })

  it('should login a user', () => {
    let sessionMock: LoginRequest = {
      email: "test@test.com",
      password: 'string'
    }
    const responseMock: SessionInformation = {
      token: 'test-token',
      type: 'Bearer',
      id: 1,
      username: 'test',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };

    service.login(sessionMock).subscribe(response => {
      expect(response).toEqual(responseMock)
    })
    const req = httpMock.expectOne(`${service['pathService']}/login`)
    expect(req.request.method).toBe('POST')
    req.flush(null)
  })
});
