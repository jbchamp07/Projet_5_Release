import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;
  const mockSession: Session = {
    id: 1, name: 'Test Session',
    description: '',
    date: new Date('2024-11-23'),
    teacher_id: 0,
    users: []
  };  // Un exemple de session

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();  // Vérifie qu'aucune requête en attente n'existe après chaque test
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('all()', () => {
    it('should fetch all sessions', () => {
      service.all().subscribe((sessions) => {
        expect(sessions.length).toBe(1);
        expect(sessions[0].name).toBe('Test Session');
      });

      const req = httpMock.expectOne('api/session');
      expect(req.request.method).toBe('GET');
      req.flush([mockSession]);
    });
  });

  describe('detail()', () => {
    it('should fetch a session by id', () => {
      const sessionId = '1';

      service.detail(sessionId).subscribe((session) => {
        expect(session.id).toBe(sessionId);
        expect(session.name).toBe('Test Session');
      });

      const req = httpMock.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockSession);
    });
  });

  describe('delete()', () => {
    it('should delete a session by id', () => {
      const sessionId = '1';

      service.delete(sessionId).subscribe((response) => {
        expect(response).toBeNull();  // On s'attend à une réponse vide
      });

      const req = httpMock.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);  // Réponse vide pour DELETE
    });
  });

  describe('create()', () => {
    it('should create a new session', () => {
      const newSession: Session = {
        name: '',
        description: '',
        date: new Date('2024-11-23'),
        teacher_id: 0,
        users: []
      };

      service.create(newSession).subscribe((session) => {
        expect(session.id).toBe('2');
        expect(session.name).toBe('New Session');
      });

      const req = httpMock.expectOne('api/session');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(newSession);
      req.flush(newSession);
    });
  });

  describe('update()', () => {
    it('should update a session', () => {
      const updatedSession: Session = {
        name: '',
        description: '',
        date: new Date('2024-11-23'),
        teacher_id: 0,
        users: []
      };

      service.update('1', updatedSession).subscribe((session) => {
        expect(session.name).toBe('Updated Session');
      });

      const req = httpMock.expectOne('api/session/1');
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updatedSession);
      req.flush(updatedSession);
    });
  });

  describe('participate()', () => {
    it('should allow a user to participate in a session', () => {
      const sessionId = '1';
      const userId = 'user1';

      service.participate(sessionId, userId).subscribe(() => {
        expect(true).toBeTruthy();  // Si la requête réussit, le test passe
      });

      const req = httpMock.expectOne(`api/session/1/participate/user1`);
      expect(req.request.method).toBe('POST');
      req.flush(null);  // Pas de corps de réponse
    });
  });

  describe('unParticipate()', () => {
    it('should allow a user to unparticipate from a session', () => {
      const sessionId = '1';
      const userId = 'user1';

      service.unParticipate(sessionId, userId).subscribe(() => {
        expect(true).toBeTruthy();  // Si la requête réussit, le test passe
      });

      const req = httpMock.expectOne(`api/session/1/participate/user1`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);  // Pas de corps de réponse
    });
  });
});
