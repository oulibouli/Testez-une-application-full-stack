import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start with isLogged is false', () => {
    expect(service.isLogged).toBeFalsy()
  })

  it('should set user into sessionInformation and isLogged to true', () => {
    const mockSession: SessionInformation =  {
      token: '1234',
      type: 'Bearer',
      id: 1,
      username: 'username',
      firstName: 'user',
      lastName: 'name',
      admin: false
    };
    service.logIn(mockSession);
    expect(service.isLogged).toBeTruthy()
    expect(service.sessionInformation).toEqual(mockSession)
  })

  it('should set isLogged to false and sessionInformation to undefined', () => {
    const mockSession: SessionInformation =  {
      token: '1234',
      type: 'Bearer',
      id: 1,
      username: 'username',
      firstName: 'user',
      lastName: 'name',
      admin: false
    };
    service.logIn(mockSession);
    service.logOut()
    expect(service.sessionInformation).toBeUndefined()
    expect(service.isLogged).toBeFalsy()
  })

  // Manage async test with done method
  it('should emit correct value from observable $isLogged', (done) => {
    const mockSession: SessionInformation =  {
      token: '1234',
      type: 'Bearer',
      id: 1,
      username: 'username',
      firstName: 'user',
      lastName: 'name',
      admin: false
    };

    let asyncCheck = false
    service.$isLogged().subscribe(isLogged => {
      if(!asyncCheck){
        expect(isLogged).toBeFalsy()
        service.logIn(mockSession);
        asyncCheck=true
      }
      else {
        expect(isLogged).toBeTruthy()
        done()
      }
    })
    done()
  })
});
