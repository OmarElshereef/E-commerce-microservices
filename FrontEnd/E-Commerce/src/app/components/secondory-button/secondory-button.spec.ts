import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SecondoryButton } from './secondory-button';

describe('SecondoryButton', () => {
  let component: SecondoryButton;
  let fixture: ComponentFixture<SecondoryButton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SecondoryButton]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SecondoryButton);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
