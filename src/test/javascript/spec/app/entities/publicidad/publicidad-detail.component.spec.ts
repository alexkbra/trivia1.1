/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TriviaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PublicidadDetailComponent } from '../../../../../../main/webapp/app/entities/publicidad/publicidad-detail.component';
import { PublicidadService } from '../../../../../../main/webapp/app/entities/publicidad/publicidad.service';
import { Publicidad } from '../../../../../../main/webapp/app/entities/publicidad/publicidad.model';

describe('Component Tests', () => {

    describe('Publicidad Management Detail Component', () => {
        let comp: PublicidadDetailComponent;
        let fixture: ComponentFixture<PublicidadDetailComponent>;
        let service: PublicidadService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TriviaTestModule],
                declarations: [PublicidadDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PublicidadService,
                    JhiEventManager
                ]
            }).overrideTemplate(PublicidadDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PublicidadDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PublicidadService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Publicidad(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.publicidad).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
