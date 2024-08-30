import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  it('should get by id', () => {
    const mockUser: User = { id: 1, firstName: 'John', lastName: 'Doe' } as User;
    
    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUser);
    });
  })
  it('should delete by id', () => {
    const mockUser: User = { id: 1, firstName: 'John', lastName: 'Doe' } as User;
    
    service.delete('1').subscribe(response => {
      expect(response).toBeNull;
    });
  })
});
