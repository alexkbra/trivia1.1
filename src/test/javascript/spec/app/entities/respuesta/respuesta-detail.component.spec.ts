/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TriviaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RespuestaDetailComponent } from '../../../../../../main/webapp/app/entities/respuesta/respuesta-detail.component';
import { RespuestaService } from '../../../../../../main/webapp/app/entities/respuesta/respuesta.service';
import { Respuesta } from '../../../../../../main/webapp/app/entities/respuesta/respuesta.model';

describe('Component Tests', () => {

    describe('Respuesta Management Detail Component', () => {
        let comp: RespuestaDetailComponent;
        let fixture: ComponentFixture<RespuestaDetailComponent>;
        let service: RespuestaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TriviaTestModule],
                declarations: [RespuestaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RespuestaService,
                    JhiEventManager
                ]
            }).overrideTemplate(RespuestaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RespuestaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RespuestaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Respuesta(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.respuesta).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
