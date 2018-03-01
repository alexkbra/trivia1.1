/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TriviaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TipoPreguntaDetailComponent } from '../../../../../../main/webapp/app/entities/tipo-pregunta/tipo-pregunta-detail.component';
import { TipoPreguntaService } from '../../../../../../main/webapp/app/entities/tipo-pregunta/tipo-pregunta.service';
import { TipoPregunta } from '../../../../../../main/webapp/app/entities/tipo-pregunta/tipo-pregunta.model';

describe('Component Tests', () => {

    describe('TipoPregunta Management Detail Component', () => {
        let comp: TipoPreguntaDetailComponent;
        let fixture: ComponentFixture<TipoPreguntaDetailComponent>;
        let service: TipoPreguntaService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TriviaTestModule],
                declarations: [TipoPreguntaDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TipoPreguntaService,
                    JhiEventManager
                ]
            }).overrideTemplate(TipoPreguntaDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TipoPreguntaDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TipoPreguntaService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TipoPregunta(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.tipoPregunta).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
