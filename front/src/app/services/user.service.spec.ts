import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController)
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  it('should get by id', () => {
    const mockUser: User = { id: 1, firstName: 'John', lastName: 'Doe' } as User;
    
    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req: TestRequest = httpTestingController.expectOne(`api/user/1`);
    expect(req.request.method).toBe('GET');
  })
  it('should delete by id', () => {  
    service.delete('1').subscribe(response => {
      expect(response).toBeNull;
    });
  })
});
