import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartnerSelectedComponent } from './partner-selected.component';

describe('PartnerSelectedComponent', () => {
  let component: PartnerSelectedComponent;
  let fixture: ComponentFixture<PartnerSelectedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PartnerSelectedComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PartnerSelectedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
