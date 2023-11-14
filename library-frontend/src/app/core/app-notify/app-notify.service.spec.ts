import { TestBed } from '@angular/core/testing';

import { AppNotifyService } from './app-notify.service';

describe('AppNotifyService', () => {
  let service: AppNotifyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppNotifyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
