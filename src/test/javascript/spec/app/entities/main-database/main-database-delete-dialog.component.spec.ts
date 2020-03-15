/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DescaTestModule } from '../../../test.module';
import { MainDatabaseDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/main-database/main-database-delete-dialog.component';
import { MainDatabaseService } from '../../../../../../main/webapp/app/entities/main-database/main-database.service';

describe('Component Tests', () => {

    describe('MainDatabase Management Delete Component', () => {
        let comp: MainDatabaseDeleteDialogComponent;
        let fixture: ComponentFixture<MainDatabaseDeleteDialogComponent>;
        let service: MainDatabaseService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [MainDatabaseDeleteDialogComponent],
                providers: [
                    MainDatabaseService
                ]
            })
            .overrideTemplate(MainDatabaseDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MainDatabaseDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MainDatabaseService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
