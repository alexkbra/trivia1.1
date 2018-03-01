/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TriviaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { NivelDetailComponent } from '../../../../../../main/webapp/app/entities/nivel/nivel-detail.component';
import { NivelService } from '../../../../../../main/webapp/app/entities/nivel/nivel.service';
import { Nivel } from '../../../../../../main/webapp/app/entities/nivel/nivel.model';

describe('Component Tests', () => {

    describe('Nivel Management Detail Component', () => {
        let comp: NivelDetailComponent;
        let fixture: ComponentFixture<NivelDetailComponent>;
        let service: NivelService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TriviaTestModule],
                declarations: [NivelDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    NivelService,
                    JhiEventManager
                ]
            }).overrideTemplate(NivelDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(NivelDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NivelService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Nivel(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.nivel).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
