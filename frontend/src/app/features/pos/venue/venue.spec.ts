import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Venue } from './venue';

describe('Venue', () => {
  let component: Venue;
  let fixture: ComponentFixture<Venue>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Venue],
    }).compileComponents();

    fixture = TestBed.createComponent(Venue);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
