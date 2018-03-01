/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TriviaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ReglaDetailComponent } from '../../../../../../main/webapp/app/entities/regla/regla-detail.component';
import { ReglaService } from '../../../../../../main/webapp/app/entities/regla/regla.service';
import { Regla } from '../../../../../../main/webapp/app/entities/regla/regla.model';

describe('Component Tests', () => {

    describe('Regla Management Detail Component', () => {
        let comp: ReglaDetailComponent;
        let fixture: ComponentFixture<ReglaDetailComponent>;
        let service: ReglaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TriviaTestModule],
                declarations: [ReglaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ReglaService,
                    JhiEventManager
                ]
            }).overrideTemplate(ReglaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ReglaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReglaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Regla(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.regla).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
