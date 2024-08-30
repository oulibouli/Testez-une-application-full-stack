import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { User } from 'src/app/interfaces/user.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  const mockedSession : Partial<Session> = { id: 1, name: 'Session 1', description: 'Description 1', date: new Date() };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  it('should return sessions details', () => {
    service.detail('1').subscribe(session => expect(session).toEqual(mockedSession));
  })
  it('should delete the session', () => {
    service.delete('2').subscribe(session => expect(session).toBeNull)
  })
  it('should create the session', () => {
    const mockedSession : Session = { users:[], teacher_id:1, id: 2, name: 'Session 2', description: 'Description 2', date: new Date() };
    service.create(mockedSession).subscribe(session => expect(session).toEqual(mockedSession))
  })
  it('should update the session', () => {
    const newmockedSession : Session = { users:[], teacher_id:1, id: 2, name: 'New name', description: 'Description 2', date: new Date() };
    service.update('2', newmockedSession).subscribe(session => expect(session.name).toEqual(newmockedSession.name))
  })
  it('should participate the session', () => {
    service.participate('1', '1').subscribe();
  })
  it('should unparticipate the session', () => {
    service.unParticipate('2', '1').subscribe();
  })
});
