/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DescaTestModule } from '../../../test.module';
import { InternationalStandarDialogComponent } from '../../../../../../main/webapp/app/entities/international-standar/international-standar-dialog.component';
import { InternationalStandarService } from '../../../../../../main/webapp/app/entities/international-standar/international-standar.service';
import { InternationalStandar } from '../../../../../../main/webapp/app/entities/international-standar/international-standar.model';

describe('Component Tests', () => {

    describe('InternationalStandar Management Dialog Component', () => {
        let comp: InternationalStandarDialogComponent;
        let fixture: ComponentFixture<InternationalStandarDialogComponent>;
        let service: InternationalStandarService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [InternationalStandarDialogComponent],
                providers: [
                    InternationalStandarService
                ]
            })
            .overrideTemplate(InternationalStandarDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InternationalStandarDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InternationalStandarService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new InternationalStandar(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.internationalStandar = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'internationalStandarListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new InternationalStandar();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.internationalStandar = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'internationalStandarListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
