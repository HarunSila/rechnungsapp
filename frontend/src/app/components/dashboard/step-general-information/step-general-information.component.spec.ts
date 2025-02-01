import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StepGeneralInformationComponent } from './step-general-information.component';

describe('StepGeneralInformationComponent', () => {
  let component: StepGeneralInformationComponent;
  let fixture: ComponentFixture<StepGeneralInformationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StepGeneralInformationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StepGeneralInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
