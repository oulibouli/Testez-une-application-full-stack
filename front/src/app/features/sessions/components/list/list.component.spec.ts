import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { By } from '@angular/platform-browser';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [{ provide: SessionService, useValue: mockSessionService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call user and return the session infos', () => {
    expect(component.user).toEqual(mockSessionService.sessionInformation)
  })
  it('should display create and detail button if admin', () => {
    // Detect changes in the template
    fixture.detectChanges()
    const createButton = fixture.debugElement.query(By.css('button[routerLink="create"]'))
    expect(createButton).toBeTruthy

    const detailButton = fixture.debugElement.query(By.css(`button[routerLink="['detail',session.id]"]`))
    expect(detailButton).toBeTruthy
  })

  it('should not display create and detail button if not admin', () => {
    mockSessionService.sessionInformation.admin = false
    // Detect changes in the template
    fixture.detectChanges()
    const createButton = fixture.debugElement.query(By.css('button[routerLink="create"]'))
    expect(createButton).toBeFalsy

    const detailButton = fixture.debugElement.query(By.css(`button[routerLink="['detail',session.id]"]`))
    expect(detailButton).toBeFalsy
  })
});
