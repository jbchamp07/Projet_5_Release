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

  it('should start with isLogged as false', () => {
    expect(service.isLogged).toBe(false);
  });

  it('should start with sessionInformation as undefined', () => {
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should update isLogged to true and sessionInformation on logIn', () => {
    const user: SessionInformation = {
      username: 'testUser',
      token: '',
      type: '',
      id: 0,
      firstName: '',
      lastName: '',
      admin: false
    };
    service.logIn(user);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(user);
  });

  it('should update isLogged to false and sessionInformation to undefined on logOut', () => {
    const user: SessionInformation = {
      username: 'testUser',
      token: '',
      type: '',
      id: 0,
      firstName: '',
      lastName: '',
      admin: false
    };
    service.logIn(user); // First, log in the user
    service.logOut(); // Then, log out

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  
});
