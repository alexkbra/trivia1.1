/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TriviaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { GaleriasDetailComponent } from '../../../../../../main/webapp/app/entities/galerias/galerias-detail.component';
import { GaleriasService } from '../../../../../../main/webapp/app/entities/galerias/galerias.service';
import { Galerias } from '../../../../../../main/webapp/app/entities/galerias/galerias.model';

describe('Component Tests', () => {

    describe('Galerias Management Detail Component', () => {
        let comp: GaleriasDetailComponent;
        let fixture: ComponentFixture<GaleriasDetailComponent>;
        let service: GaleriasService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TriviaTestModule],
                declarations: [GaleriasDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    GaleriasService,
                    JhiEventManager
                ]
            }).overrideTemplate(GaleriasDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GaleriasDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GaleriasService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Galerias(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.galerias).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
