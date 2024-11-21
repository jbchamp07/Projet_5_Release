import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { MeComponent } from './me.component';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  let mockSessionService: any;
  let mockUserService: any;
  let mockMatSnackBar: any;
  let mockRouter: any;

  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: { admin: true, id: 1 },
      logOut: jest.fn()
    };

    mockUserService = {
      getById: jest.fn().mockReturnValue(of({ id: 1, name: 'Test User' })),
      delete: jest.fn().mockReturnValue(of({}))
    };

    mockMatSnackBar = {
      open: jest.fn()
    };

    mockRouter = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [MatSnackBarModule, HttpClientModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user data on ngOnInit', () => {
    // Vérification que `getById` est appelé avec le bon ID
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    // Vérification que les données utilisateur sont assignées
    expect(component.user).toEqual({ id: 1, name: 'Test User' });
  });

  it('should navigate back on back()', () => {
    // Espionnage de window.history.back
    const historyBackSpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(historyBackSpy).toHaveBeenCalled();
  });

  it('should delete user account and log out on delete()', () => {
    component.delete();

    // Vérification que le service `delete` est appelé
    expect(mockUserService.delete).toHaveBeenCalledWith('1');

    // Vérification que le snackbar affiche le bon message
    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );

    // Vérification que logOut est appelé
    expect(mockSessionService.logOut).toHaveBeenCalled();

    // Vérification de la navigation
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});