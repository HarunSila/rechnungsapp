import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StepLastConfirmationComponent } from './step-last-confirmation.component';

describe('StepLastConfirmationComponent', () => {
  let component: StepLastConfirmationComponent;
  let fixture: ComponentFixture<StepLastConfirmationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StepLastConfirmationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StepLastConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
