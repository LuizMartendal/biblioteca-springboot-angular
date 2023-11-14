import { TestBed } from '@angular/core/testing';

import { AuthRedirectGuard } from './auth-redirect.guard';

describe('AuthRedirectGuard', () => {
  let guard: AuthRedirectGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(AuthRedirectGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
