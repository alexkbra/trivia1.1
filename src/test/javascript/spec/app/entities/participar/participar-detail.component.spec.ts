/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TriviaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ParticiparDetailComponent } from '../../../../../../main/webapp/app/entities/participar/participar-detail.component';
import { ParticiparService } from '../../../../../../main/webapp/app/entities/participar/participar.service';
import { Participar } from '../../../../../../main/webapp/app/entities/participar/participar.model';

describe('Component Tests', () => {

    describe('Participar Management Detail Component', () => {
        let comp: ParticiparDetailComponent;
        let fixture: ComponentFixture<ParticiparDetailComponent>;
        let service: ParticiparService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TriviaTestModule],
                declarations: [ParticiparDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ParticiparService,
                    JhiEventManager
                ]
            }).overrideTemplate(ParticiparDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ParticiparDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParticiparService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Participar(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.participar).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
