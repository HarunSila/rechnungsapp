import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StepTextTemplateComponent } from './step-text-template.component';

describe('StepTextTemplateComponent', () => {
  let component: StepTextTemplateComponent;
  let fixture: ComponentFixture<StepTextTemplateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StepTextTemplateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StepTextTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
