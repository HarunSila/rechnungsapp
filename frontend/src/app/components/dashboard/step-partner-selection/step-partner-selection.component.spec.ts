import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StepPartnerSelectionComponent } from './step-partner-selection.component';

describe('StepPartnerSelectionComponent', () => {
  let component: StepPartnerSelectionComponent;
  let fixture: ComponentFixture<StepPartnerSelectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StepPartnerSelectionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StepPartnerSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
