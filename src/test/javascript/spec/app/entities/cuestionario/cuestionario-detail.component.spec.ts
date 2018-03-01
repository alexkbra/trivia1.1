/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TriviaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CuestionarioDetailComponent } from '../../../../../../main/webapp/app/entities/cuestionario/cuestionario-detail.component';
import { CuestionarioService } from '../../../../../../main/webapp/app/entities/cuestionario/cuestionario.service';
import { Cuestionario } from '../../../../../../main/webapp/app/entities/cuestionario/cuestionario.model';

describe('Component Tests', () => {

    describe('Cuestionario Management Detail Component', () => {
        let comp: CuestionarioDetailComponent;
        let fixture: ComponentFixture<CuestionarioDetailComponent>;
        let service: CuestionarioService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TriviaTestModule],
                declarations: [CuestionarioDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CuestionarioService,
                    JhiEventManager
                ]
            }).overrideTemplate(CuestionarioDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CuestionarioDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CuestionarioService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Cuestionario(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cuestionario).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
