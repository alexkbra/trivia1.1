/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TriviaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PistaDetailComponent } from '../../../../../../main/webapp/app/entities/pista/pista-detail.component';
import { PistaService } from '../../../../../../main/webapp/app/entities/pista/pista.service';
import { Pista } from '../../../../../../main/webapp/app/entities/pista/pista.model';

describe('Component Tests', () => {

    describe('Pista Management Detail Component', () => {
        let comp: PistaDetailComponent;
        let fixture: ComponentFixture<PistaDetailComponent>;
        let service: PistaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TriviaTestModule],
                declarations: [PistaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PistaService,
                    JhiEventManager
                ]
            }).overrideTemplate(PistaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PistaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PistaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Pista(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.pista).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
