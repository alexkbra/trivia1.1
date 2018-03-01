/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TriviaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EmpresasDetailComponent } from '../../../../../../main/webapp/app/entities/empresas/empresas-detail.component';
import { EmpresasService } from '../../../../../../main/webapp/app/entities/empresas/empresas.service';
import { Empresas } from '../../../../../../main/webapp/app/entities/empresas/empresas.model';

describe('Component Tests', () => {

    describe('Empresas Management Detail Component', () => {
        let comp: EmpresasDetailComponent;
        let fixture: ComponentFixture<EmpresasDetailComponent>;
        let service: EmpresasService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TriviaTestModule],
                declarations: [EmpresasDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EmpresasService,
                    JhiEventManager
                ]
            }).overrideTemplate(EmpresasDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EmpresasDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmpresasService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Empresas(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.empresas).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
